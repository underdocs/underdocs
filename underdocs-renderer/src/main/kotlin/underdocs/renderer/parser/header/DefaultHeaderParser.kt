package underdocs.renderer.parser.header

import com.vladsch.flexmark.parser.Parser
import underdocs.renderer.representation.FunctionParameter
import underdocs.renderer.representation.Header
import underdocs.renderer.representation.ReferredType
import underdocs.renderer.representation.StructType
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.documentation.Documentation
import underdocs.representation.EnumElement
import underdocs.representation.EnumMember
import underdocs.representation.EnumType
import underdocs.representation.Function
import underdocs.representation.MacroConstant
import underdocs.representation.MacroFunction
import underdocs.representation.Member
import underdocs.representation.Struct
import underdocs.representation.StructMember
import underdocs.representation.Type
import underdocs.representation.TypeSynonym
import underdocs.representation.Union
import underdocs.representation.UnionMember
import underdocs.representation.UnionType
import underdocs.representation.Variable
import underdocs.representation.VariableMember
import underdocs.representation.visitor.BaseElementVisitor
import underdocs.representation.Header as CommonHeader

class DefaultHeaderParser(private val mdParser: Parser): underdocs.renderer.parser.header.HeaderParser {
    private val headerDocumentationParser = underdocs.renderer.parser.documentation.HeaderDocumentationParser()

    override fun parse(header: CommonHeader): Header {
        val visitor = underdocs.renderer.parser.header.DefaultHeaderParser.ElementVisitor(header, mdParser)

        val elements = visitor.transformElements()

        val documentation = header.comment
                ?.let { mdParser.parse(it) }
                ?.let { headerDocumentationParser.parse(it )}

        val parsedHeader = Header(
                header.path,
                header.filename,
                null,
                elements,
                documentation
        )

        return parsedHeader
    }

    private class ElementVisitor(private val header: CommonHeader, private val mdParser: Parser): BaseElementVisitor() {
        companion object {
            // TODO: Move this to somewhere else
            private const val UNKNOWN_GROUP = "UNKNOWN";
        }

        private val enumElementDocumentationParser = underdocs.renderer.parser.documentation.EnumElementDocumentationParser()
        private val functionDocumentationParser = underdocs.renderer.parser.documentation.FunctionDocumentationParser()
        private val macroConstantDocumentationParser = underdocs.renderer.parser.documentation.MacroConstantDocumentationParser()
        private val macroFunctionDocumentationParser = underdocs.renderer.parser.documentation.MacroFunctionDocumentationParser()
        private val memberDocumentationParser = underdocs.renderer.parser.documentation.MemberDocumentationParser()
        private val structDocumentationParser = underdocs.renderer.parser.documentation.StructDocumentationParser()
        private val typeSynonymDocumentationParser = underdocs.renderer.parser.documentation.TypeSynonymDocumentationParser()
        private val unionDocumentationParser = underdocs.renderer.parser.documentation.UnionDocumentationParser()
        private val variableDocumentationParser = underdocs.renderer.parser.documentation.VariableDocumentationParser()

        private val elements = mutableMapOf<String, MutableList<TopLevelElement>>()

        fun transformElements(): Map<String, List<TopLevelElement>> {
            header.elements.forEach {
                it.accept(this)
            }

            return elements
        }

        private fun recordElement(group: String?, element: TopLevelElement) {
            val actualGroup = group ?: underdocs.renderer.parser.header.DefaultHeaderParser.ElementVisitor.Companion.UNKNOWN_GROUP

            if (actualGroup !in elements) {
                elements[actualGroup] = mutableListOf()
            }

            elements[actualGroup]?.add(element)
        }

        private fun <T: Documentation> parseGroupedDocumentation(comment: String?, parser: underdocs.renderer.parser.documentation.DocumentationParser<T>): Pair<String?, T?> {
            val documentation = comment
                    ?.let { mdParser.parse(it) }
                    ?.let { parser.parse(it) }

            val group = documentation?.getAttributes()?.get("group")

            return Pair(group, documentation)
        }

        private fun <T: Documentation> parseDocumentation(comment: String?, parser: underdocs.renderer.parser.documentation.DocumentationParser<T>) =
                comment
                    ?.let { mdParser.parse(it) }
                    ?.let { parser.parse(it) }

        override fun visit(element: EnumElement) {
            val (group, documentation) = parseGroupedDocumentation(element.comment, enumElementDocumentationParser)

            val members = element.members.map {
                underdocs.renderer.representation.EnumConstant(
                        it.getStartingLine(),
                        it.getRaw(),
                        it.name,
                        it.value,
                        parseDocumentation(it.comment, memberDocumentationParser)
                )
            }

            val result = underdocs.renderer.representation.EnumElement(
                    element.getStartingLine(),
                    element.getRaw(),
                    null,
                    element.name,
                    element.typedef,
                    element.specifiers,
                    members,
                    documentation
            )

            recordElement(group, result)
        }

        override fun visit(element: Function) {
            val (group, documentation) = parseGroupedDocumentation(element.comment, functionDocumentationParser)

            val parameters = element.parameters.map {
                FunctionParameter(
                        it.getStartingLine(),
                        it.getRaw(),
                        it.name,
                        processReferredType(it.type)
                )
            }

            val result = underdocs.renderer.representation.Function(
                    element.getStartingLine(),
                    element.getRaw(),
                    null,
                    element.name,
                    element.specifiers,
                    processReferredType(element.returnType),
                    parameters,
                    documentation
            )

            recordElement(group, result)
        }

        override fun visit(element: MacroConstant) {
            val (group, documentation) = parseGroupedDocumentation(element.comment, macroConstantDocumentationParser)

            val result = underdocs.renderer.representation.MacroConstant(
                    element.getStartingLine(),
                    element.getRaw(),
                    null,
                    element.name,
                    element.expansion,
                    documentation
            )

            recordElement(group, result)
        }

        override fun visit(element: MacroFunction) {
            val (group, documentation) = parseGroupedDocumentation(element.comment, macroFunctionDocumentationParser)

            val result = underdocs.renderer.representation.MacroFunction(
                    element.getStartingLine(),
                    element.getRaw(),
                    null,
                    element.name,
                    element.expansion,
                    element.parameters,
                    documentation
            )

            recordElement(group, result)
        }

        override fun visit(element: Struct) {
            val (group, documentation) = parseGroupedDocumentation(element.comment, structDocumentationParser)

            val result = underdocs.renderer.representation.Struct(
                    element.getStartingLine(),
                    element.getRaw(),
                    null,
                    element.name,
                    element.typedef,
                    element.specifiers,
                    element.members.map { processMember(it) },
                    documentation
            )

            recordElement(group, result)
        }

        override fun visit(element: TypeSynonym) {
            val (group, documentation) = parseGroupedDocumentation(element.comment, typeSynonymDocumentationParser)

            val result = underdocs.renderer.representation.TypeSynonym(
                    element.getStartingLine(),
                    element.getRaw(),
                    null,
                    element.originalName,
                    element.newName,
                    documentation
            )

            recordElement(group, result)
        }

        override fun visit(element: Union) {
            val (group, documentation) = parseGroupedDocumentation(element.comment, unionDocumentationParser)

            val result = underdocs.renderer.representation.Union(
                    element.getStartingLine(),
                    element.getRaw(),
                    null,
                    element.name,
                    element.typedef,
                    element.specifiers,
                    element.members.map { processMember(it) },
                    documentation
            )

            recordElement(group, result)
        }

        override fun visit(element: Variable) {
            val (group, documentation) = parseGroupedDocumentation(element.comment, variableDocumentationParser)

            val result = underdocs.renderer.representation.Variable(
                    element.getStartingLine(),
                    element.getRaw(),
                    null,
                    element.name,
                    element.specifiers,
                    processType(element.type),
                    documentation
            )

            recordElement(group, result)
        }

        private fun processMember(member: Member): underdocs.renderer.representation.Member =
                when (member) {
                    is StructMember -> processStructMember(member)
                    is UnionMember -> processUnionMember(member)
                    is EnumMember -> processEnumMember(member)
                    is VariableMember -> processVariableMember(member)
                    else -> throw IllegalStateException("I hope, you're happy, Kotlin!")
                }

        private fun processStructMember(member: StructMember) =
                underdocs.renderer.representation.StructMember(
                        member.getStartingLine(),
                        member.getRaw(),
                        member.name,
                        member.members.map { processMember(it) },
                        parseDocumentation(member.comment, memberDocumentationParser)
                )

        private fun processUnionMember(member: UnionMember) =
                underdocs.renderer.representation.UnionMember(
                        member.getStartingLine(),
                        member.getRaw(),
                        member.name,
                        member.members.map { processMember(it) },
                        parseDocumentation(member.comment, memberDocumentationParser)
                )

        private fun processEnumMember(member: EnumMember) =
                underdocs.renderer.representation.EnumMember(
                        member.getStartingLine(),
                        member.getRaw(),
                        member.name,
                        member.members.map {
                            underdocs.renderer.representation.EnumConstant(
                                    it.getStartingLine(),
                                    it.getRaw(),
                                    it.name,
                                    it.value,
                                    parseDocumentation(it.comment, memberDocumentationParser)
                            )
                        },
                        parseDocumentation(member.comment, memberDocumentationParser)
                )

        private fun processVariableMember(member: VariableMember) =
                underdocs.renderer.representation.VariableMember(
                        member.getStartingLine(),
                        member.getRaw(),
                        member.name,
                        member.specifiers,
                        processType(member.type),
                        parseDocumentation(member.comment, memberDocumentationParser)
                )

        private fun processType(type: Type): underdocs.renderer.representation.Type =
                when (type) {
                    is underdocs.representation.StructType -> processStructType(type)
                    is UnionType -> processUnionType(type)
                    is underdocs.representation.ReferredType -> processReferredType(type)
                    is EnumType -> processEnumType(type)
                    else -> throw IllegalStateException("I hope, you're happy, Kotlin!")
                }

        private fun processStructType(type: underdocs.representation.StructType) =
                StructType(
                        type.getStartingLine(),
                        type.getRaw(),
                        type.name,
                        type.members.map { processMember(it) }
                )

        private fun processUnionType(type: UnionType) =
                underdocs.renderer.representation.UnionType(
                        type.getStartingLine(),
                        type.getRaw(),
                        type.name,
                        type.members.map { processMember(it) }
                )

        private fun processReferredType(type: underdocs.representation.ReferredType) =
                ReferredType(
                        type.name,
                        type.specifiers
                )

        private fun processEnumType(type: EnumType) =
                underdocs.renderer.representation.EnumType(
                        type.getStartingLine(),
                        type.getRaw(),
                        type.name,
                        type.members.map {
                            underdocs.renderer.representation.EnumConstant(
                                    it.getStartingLine(),
                                    it.getRaw(),
                                    it.name,
                                    it.value,
                                    parseDocumentation(it.comment, memberDocumentationParser)
                            )
                        }
                )
    }
}
