package underdocs.parser

import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier
import org.eclipse.cdt.core.dom.ast.IASTDeclaration
import org.eclipse.cdt.core.dom.ast.IASTFunctionDeclarator
import org.eclipse.cdt.core.dom.ast.IASTNode
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorFunctionStyleMacroDefinition
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIfStatement
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIfndefStatement
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorMacroDefinition
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorObjectStyleMacroDefinition
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorStatement
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration
import org.eclipse.cdt.core.dom.ast.IASTStandardFunctionDeclarator
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit
import org.eclipse.cdt.core.dom.ast.c.ICASTCompositeTypeSpecifier
import org.eclipse.cdt.core.dom.ast.c.ICASTElaboratedTypeSpecifier
import org.eclipse.cdt.core.dom.ast.c.ICASTEnumerationSpecifier
import org.eclipse.cdt.core.dom.ast.c.ICASTSimpleDeclSpecifier
import org.eclipse.cdt.core.dom.ast.c.ICASTTypedefNameSpecifier
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage
import org.eclipse.cdt.core.dom.ast.gnu.c.ICASTKnRFunctionDeclarator
import org.eclipse.cdt.core.model.ILanguage.OPTION_SKIP_FUNCTION_BODIES
import org.eclipse.cdt.core.parser.DefaultLogService
import org.eclipse.cdt.core.parser.FileContent
import org.eclipse.cdt.core.parser.IParserLogService
import org.eclipse.cdt.core.parser.IncludeFileContentProvider
import org.eclipse.cdt.core.parser.ScannerInfo
import org.eclipse.cdt.internal.core.dom.rewrite.commenthandler.ASTCommenter.getCommentedNodeMap
import org.eclipse.cdt.internal.core.dom.rewrite.commenthandler.NodeCommentMap
import underdocs.parser.comment.EclipseCommentProcessor
import underdocs.representation.Element
import underdocs.representation.EnumConstant
import underdocs.representation.EnumElement
import underdocs.representation.EnumMember
import underdocs.representation.EnumType
import underdocs.representation.Function
import underdocs.representation.FunctionParameter
import underdocs.representation.Header
import underdocs.representation.MacroConstant
import underdocs.representation.MacroFunction
import underdocs.representation.Member
import underdocs.representation.ReferredType
import underdocs.representation.Struct
import underdocs.representation.StructMember
import underdocs.representation.StructType
import underdocs.representation.Type
import underdocs.representation.TypeSynonym
import underdocs.representation.Union
import underdocs.representation.UnionMember
import underdocs.representation.UnionType
import underdocs.representation.Variable
import underdocs.representation.VariableMember
import underdocs.parser.configuration.EclipseParserConfiguration
import java.nio.file.Paths

class EclipseHeaderParser(private val configuration: EclipseParserConfiguration) : HeaderParser {
    private val logService: IParserLogService = DefaultLogService()

    override fun parse(path: String): Header {
        val parseTree = createParseTree(path)

        val comments = getCommentedNodeMap(parseTree)

        val commentProcessor = EclipseCommentProcessor(configuration.documentationCommentStyles, true)

        return HeaderBuilder(path, parseTree, comments, commentProcessor).createHeader()
    }

    private fun createParseTree(path: String): IASTTranslationUnit {
        val definedSymbols = emptyMap<String, String>()

        val info = ScannerInfo(definedSymbols)

        val fileContent = FileContent.createForExternalFileLocation(path)

        val emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider()

        val options = OPTION_SKIP_FUNCTION_BODIES

        return GCCLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, options, logService)
    }

    private class HeaderBuilder(private val path: String,
                                private val parseTree: IASTTranslationUnit,
                                private val commentMap: NodeCommentMap,
                                private val commentProcessor: EclipseCommentProcessor) {
        companion object {
            private const val STRUCT_KEY = 1
            private const val UNION_KEY = 2
        }

        private val elements = mutableListOf<Element>()

        private val visitedNodes = mutableListOf<IASTNode>()

        private var includeGuard: String? = null

        fun createHeader(): Header {
            val (headerComment, includeGuard) = extractPreamble()

            this.includeGuard = includeGuard

            parseTree.macroDefinitions
                    .mapNotNull { processMacroDefinition(it) }
                    .forEach { elements.add(it) }

            parseTree.declarations
                    .filterIsInstance<IASTSimpleDeclaration>()
                    .mapNotNull { processTopLevelDeclaration(it) }
                    .forEach { elements.add(it) }

            return Header(
                    path,
                    Paths.get(path).fileName.toString(),
                    elements,
                    headerComment
            )
        }

        private fun extractPreamble(): Pair<String?, String?> {
            val possiblePreamble = parseTree.allPreprocessorStatements
                    .firstOrNull { isIfndefOrNotDefined(it) }
                    ?: return Pair(null, null)

            val isFirstElement = parseTree.children.all {
                it == possiblePreamble || it.fileLocation.startingLineNumber > possiblePreamble.fileLocation.startingLineNumber
            }

            return if (isFirstElement) {
                Pair(commentFromNode(possiblePreamble), possiblePreamble.rawSignature)
            } else {
                Pair(null, null)
            }
        }

        private fun isIfndefOrNotDefined(statement: IASTPreprocessorStatement) =
                when (statement) {
                    is IASTPreprocessorIfStatement -> statement.condition.toString().toLowerCase().startsWith("!defined")
                    is IASTPreprocessorIfndefStatement -> true
                    else -> false
                }

        private fun isIncludeGuardDefine(macro: IASTPreprocessorObjectStyleMacroDefinition): Boolean {
            if (includeGuard == null) {
                return false
            }

            return includeGuard?.contains(rawFromNode(macro.name)) ?: false
        }

        private fun processMacroDefinition(macro: IASTPreprocessorMacroDefinition) =
                // Note, that the order of clauses DOES matter here!
                when (macro) {
                    is IASTPreprocessorFunctionStyleMacroDefinition -> makeMacroFunction(macro)
                    is IASTPreprocessorObjectStyleMacroDefinition ->
                        if (isIncludeGuardDefine(macro)) {
                            null
                        } else {
                            makeMacroConstant(macro)
                        }
                    else -> null
                }

        private fun processTopLevelDeclaration(declaration: IASTSimpleDeclaration): Element? {
            if (declaration in visitedNodes) {
                return null
            }

            visitedNodes.add(declaration)

            return when {
                isTypeSynonym(declaration) -> makeTypeSynonym(declaration)
                isFunction(declaration) -> makeFunction(declaration)
                isEnum(declaration) -> makeTopLevelEnumElement(declaration)
                isStruct(declaration) -> makeTopLevelStruct(declaration)
                isUnion(declaration) -> makeTopLevelUnion(declaration)
                isVariable(declaration) -> makeTopLevelVariable(declaration)
                else -> null
            }
        }

        private fun isTypeSynonym(decl: IASTDeclaration) =
                decl is IASTSimpleDeclaration
                        && isTypedef(decl)
                        && decl.declSpecifier !is ICASTCompositeTypeSpecifier
                        && decl.declSpecifier !is ICASTEnumerationSpecifier

        private fun isFunction(decl: IASTDeclaration) =
                decl is IASTSimpleDeclaration
                        && decl.declarators.isNotEmpty()
                        && decl.declarators[0] is IASTFunctionDeclarator

        private fun isEnum(decl: IASTDeclaration) =
                decl is IASTSimpleDeclaration
                        && (decl.children.size == 1 || isTypedef(decl))
                        && decl.declSpecifier is ICASTEnumerationSpecifier

        private fun isStruct(decl: IASTDeclaration) =
                decl is IASTSimpleDeclaration
                        && (decl.children.size == 1 || isTypedef(decl))
                        && decl.declSpecifier is ICASTCompositeTypeSpecifier
                        && (decl.declSpecifier as ICASTCompositeTypeSpecifier).key == STRUCT_KEY

        private fun isUnion(decl: IASTDeclaration) =
                decl is IASTSimpleDeclaration
                        && (decl.children.size == 1 || isTypedef(decl))
                        && decl.declSpecifier is ICASTCompositeTypeSpecifier
                        && (decl.declSpecifier as ICASTCompositeTypeSpecifier).key == UNION_KEY

        private fun isVariable(decl: IASTDeclaration) =
                decl is IASTSimpleDeclaration
                        && decl.children.size == 2
                        && decl.declarators.isNotEmpty()
                        && decl.declarators[0] !is IASTFunctionDeclarator
                        && !isTypedef(decl)

        private fun makeMacroConstant(macro: IASTPreprocessorObjectStyleMacroDefinition) =
                MacroConstant(
                        startingLineFromNode(macro),
                        rawFromNode(macro),
                        rawFromNode(macro.name),
                        macro.expansion,
                        commentFromNode(macro)
                )

        private fun makeMacroFunction(macro: IASTPreprocessorFunctionStyleMacroDefinition) =
                MacroFunction(
                        startingLineFromNode(macro),
                        rawFromNode(macro),
                        rawFromNode(macro.name),
                        macro.expansion,
                        macroFunctionParameters(macro),
                        commentFromNode(macro)
                )

        private fun makeTypeSynonym(declaration: IASTSimpleDeclaration) =
                TypeSynonym(
                        startingLineFromNode(declaration),
                        rawFromNode(declaration),
                        rawFromNode(declaration.children[0]).removePrefix("typedef").trimStart(),
                        rawFromNode(declaration.children[1]),
                        commentFromNode(declaration)
                )

        private fun makeTopLevelEnumElement(decl: IASTSimpleDeclaration): EnumElement {
            val enumerationSpecifier = decl.declSpecifier as ICASTEnumerationSpecifier

            val declIsTypedef = isTypedef(decl)

            val name = if (declIsTypedef) {
                rawFromNode(decl.declarators[0].name)
            } else {
                rawFromNode(enumerationSpecifier.name)
            }

            return EnumElement(
                    startingLineFromNode(enumerationSpecifier),
                    rawFromNode(enumerationSpecifier),
                    name,
                    declIsTypedef,
                    emptyList(),
                    enumConstants(enumerationSpecifier),
                    commentFromNode(decl)
            )
        }

        private fun makeEnumMember(decl: IASTSimpleDeclaration): EnumMember {
            val enumerationSpecifier = decl.declSpecifier as ICASTEnumerationSpecifier

            return EnumMember(
                    startingLineFromNode(enumerationSpecifier),
                    rawFromNode(enumerationSpecifier),
                    rawFromNode(enumerationSpecifier.name),
                    enumConstants(enumerationSpecifier),
                    commentFromNode(decl)
            )
        }

        private fun makeTopLevelStruct(decl: IASTSimpleDeclaration): Struct {
            val typeSpecifier = decl.declSpecifier as ICASTCompositeTypeSpecifier

            val declIsTypedef = isTypedef(decl)

            val name = if (declIsTypedef) {
                rawFromNode(decl.declarators[0].name)
            } else {
                rawFromNode(typeSpecifier.name)
            }

            val members = typeSpecifier.members
                    .filterIsInstance<IASTSimpleDeclaration>()
                    .mapNotNull { processStructOrUnionMember(it) }
                    .toList()

            return Struct(
                    startingLineFromNode(typeSpecifier),
                    rawFromNode(typeSpecifier),
                    name,
                    declIsTypedef,
                    emptyList(),
                    members,
                    commentFromNode(typeSpecifier)
            )
        }

        private fun makeStructMember(decl: IASTSimpleDeclaration): StructMember {
            val typeSpecifier = decl.declSpecifier as ICASTCompositeTypeSpecifier

            val members = typeSpecifier.members
                    .filterIsInstance<IASTSimpleDeclaration>()
                    .mapNotNull { processStructOrUnionMember(it) }
                    .toList()

            return StructMember(
                    startingLineFromNode(typeSpecifier),
                    rawFromNode(typeSpecifier),
                    rawFromNode(typeSpecifier.name),
                    members,
                    commentFromNode(typeSpecifier)
            )
        }

        private fun processStructOrUnionMember(declaration: IASTSimpleDeclaration): Member? {
            visitedNodes.add(declaration)

            return when {
                isEnum(declaration) -> makeEnumMember(declaration)
                isStruct(declaration) -> makeStructMember(declaration)
                isUnion(declaration) -> makeUnionMember(declaration)
                isVariable(declaration) -> makeVariableMember(declaration)
                else -> null
            }
        }

        private fun isTypedef(decl: IASTSimpleDeclaration) =
                decl.declSpecifier.storageClass == IASTDeclSpecifier.sc_typedef

        private fun makeTopLevelUnion(decl: IASTSimpleDeclaration): Union {
            val typeSpecifier = decl.declSpecifier as ICASTCompositeTypeSpecifier

            val declIsTypedef = isTypedef(decl)

            val name = if (declIsTypedef) {
                rawFromNode(decl.declarators[0].name)
            } else {
                rawFromNode(typeSpecifier.name)
            }

            val members = typeSpecifier.members
                    .filterIsInstance<IASTSimpleDeclaration>()
                    .mapNotNull { processStructOrUnionMember(it) }
                    .toList()

            return Union(
                    startingLineFromNode(typeSpecifier),
                    rawFromNode(typeSpecifier),
                    name,
                    declIsTypedef,
                    emptyList(),
                    members,
                    commentFromNode(typeSpecifier)
            )
        }

        private fun makeUnionMember(decl: IASTSimpleDeclaration): UnionMember {
            val typeSpecifier = decl.declSpecifier as ICASTCompositeTypeSpecifier

            val members = typeSpecifier.members
                    .filterIsInstance<IASTSimpleDeclaration>()
                    .mapNotNull { processStructOrUnionMember(it) }
                    .toList()

            return UnionMember(
                    startingLineFromNode(typeSpecifier),
                    rawFromNode(typeSpecifier),
                    rawFromNode(typeSpecifier.name),
                    members,
                    commentFromNode(typeSpecifier)
            )
        }

        private fun makeVariableMember(decl: IASTSimpleDeclaration): VariableMember {
            val type = makeTypeFromDeclSpecifier(decl.declSpecifier)

            val declarator = decl.declarators[0]

            return VariableMember(
                    startingLineFromNode(decl),
                    rawFromNode(decl),
                    rawFromNode(declarator.name),
                    emptyList(),
                    type,
                    commentFromNode(decl)
            )
        }

        private fun makeTopLevelVariable(decl: IASTSimpleDeclaration): Variable {
            val type = makeTypeFromDeclSpecifier(decl.declSpecifier)

            val declarator = decl.declarators[0]

            return Variable(
                    startingLineFromNode(decl),
                    rawFromNode(decl),
                    rawFromNode(declarator.name),
                    emptyList(),
                    type,
                    commentFromNode(decl)
            )
        }

        private fun makeTypeFromDeclSpecifier(specifier: IASTDeclSpecifier): Type =
                when (specifier) {
                    is ICASTCompositeTypeSpecifier -> if (specifier.key == STRUCT_KEY) {
                        makeStructType(specifier)
                    } else {
                        makeUnionType(specifier)
                    }
                    is ICASTEnumerationSpecifier -> makeEnumType(specifier)
                    is ICASTElaboratedTypeSpecifier -> ReferredType(
                            startingLineFromNode(specifier),
                            rawFromNode(specifier),
                            rawFromNode(specifier.name),
                            emptyList())
                    is ICASTSimpleDeclSpecifier -> ReferredType(
                            startingLineFromNode(specifier),
                            rawFromNode(specifier),
                            rawFromNode(specifier),
                            emptyList())
                    is ICASTTypedefNameSpecifier -> ReferredType(
                            startingLineFromNode(specifier),
                            rawFromNode(specifier),
                            rawFromNode(specifier.name),
                            emptyList())
                    else -> throw IllegalStateException()
                }

        private fun makeStructType(specifier: ICASTCompositeTypeSpecifier): StructType {
            val members = specifier.members
                    .filterIsInstance<IASTSimpleDeclaration>()
                    .mapNotNull { processStructOrUnionMember(it) }
                    .toList()

            return StructType(
                    startingLineFromNode(specifier),
                    rawFromNode(specifier),
                    rawFromNode(specifier.name),
                    members
            )
        }

        private fun makeUnionType(specifier: ICASTCompositeTypeSpecifier): UnionType {
            val members = specifier.members
                    .filterIsInstance<IASTSimpleDeclaration>()
                    .mapNotNull { processStructOrUnionMember(it) }
                    .toList()

            return UnionType(
                    startingLineFromNode(specifier),
                    rawFromNode(specifier),
                    rawFromNode(specifier.name),
                    members
            )
        }

        private fun makeEnumType(specifier: ICASTEnumerationSpecifier) =
                EnumType(
                        startingLineFromNode(specifier),
                        rawFromNode(specifier),
                        rawFromNode(specifier.name),
                        enumConstants(specifier)
                )

        private fun makeFunction(decl: IASTSimpleDeclaration) =
                if (decl.declarators[0] is IASTStandardFunctionDeclarator) {
                    makeFunctionFromSimpleDeclarator(decl, decl.declarators[0] as IASTStandardFunctionDeclarator)
                } else {
                    makeFunctionFromKnRDeclarator(decl, decl.declarators[0] as ICASTKnRFunctionDeclarator)
                }

        private fun makeFunctionFromSimpleDeclarator(decl: IASTSimpleDeclaration, func: IASTStandardFunctionDeclarator) =
                Function(
                        startingLineFromNode(decl),
                        rawFromNode(decl),
                        rawFromNode(func.name),
                        emptyList(),
                        ReferredType(
                                startingLineFromNode(decl.declSpecifier),
                                rawFromNode(decl.declSpecifier),
                                rawFromNode(decl.declSpecifier),
                                emptyList()
                        ),
                        simpleFunctionParameters(func),
                        commentFromNode(decl)
                )

        private fun makeFunctionFromKnRDeclarator(decl: IASTSimpleDeclaration, func: ICASTKnRFunctionDeclarator) =
                Function(
                        startingLineFromNode(decl),
                        rawFromNode(decl),
                        rawFromNode(func.name),
                        emptyList(),
                        ReferredType(
                                startingLineFromNode(decl.declSpecifier),
                                rawFromNode(decl.declSpecifier),
                                rawFromNode(decl.declSpecifier),
                                emptyList()
                        ),
                        knrFunctionParameters(func),
                        commentFromNode(decl)
                )

        private fun startingLineFromNode(node: IASTNode) =
                node.fileLocation.startingLineNumber

        private fun rawFromNode(node: IASTNode) =
                node.rawSignature

        private fun commentFromNode(node: IASTNode): String? {
            val leadingComments = commentMap.getLeadingCommentsForNode(node)
            val trailingComments = commentMap.getTrailingCommentsForNode(node)

            return commentProcessor.extractCommentFromNodes(leadingComments, trailingComments)
        }

        private fun macroFunctionParameters(macro: IASTPreprocessorFunctionStyleMacroDefinition) =
                macro.parameters.map { it.parameter }.toList()

        private fun enumConstants(specifier: ICASTEnumerationSpecifier) =
                specifier.enumerators.map {
                    EnumConstant(
                            startingLineFromNode(it),
                            rawFromNode(it),
                            rawFromNode(it.name),
                            if (it.value == null) null else rawFromNode(it.value),
                            commentFromNode(it)
                    )
                }

        private fun simpleFunctionParameters(func: IASTStandardFunctionDeclarator) =
                func.parameters.map {
                    FunctionParameter(
                            startingLineFromNode(it),
                            rawFromNode(it),
                            rawFromNode(it.declarator),
                            ReferredType(
                                    startingLineFromNode(it.declSpecifier),
                                    rawFromNode(it.declSpecifier),
                                    rawFromNode(it.declSpecifier),
                                    emptyList()
                            )
                    )
                }

        private fun knrFunctionParameters(func: ICASTKnRFunctionDeclarator) =
                func.parameterNames.map {
                    val decl = func.getDeclaratorForParameterName(it)

                    FunctionParameter(
                            startingLineFromNode(decl),
                            rawFromNode(decl) + rawFromNode(it),
                            rawFromNode(it),
                            ReferredType(
                                    startingLineFromNode(decl),
                                    rawFromNode(decl),
                                    rawFromNode(decl),
                                    emptyList()
                            )
                    )
                }
    }
}
