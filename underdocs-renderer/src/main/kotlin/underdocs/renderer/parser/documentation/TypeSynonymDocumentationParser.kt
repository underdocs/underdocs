package underdocs.renderer.parser.documentation

import com.vladsch.flexmark.util.ast.Document
import underdocs.renderer.parser.section.SeeAlsoParser
import underdocs.renderer.parser.section.TextSectionParser
import underdocs.renderer.representation.documentation.TypeSynonymDocumentation

class TypeSynonymDocumentationParser: _root_ide_package_.underdocs.renderer.parser.documentation.DocumentationParser<TypeSynonymDocumentation> {
    private val excerptParser = _root_ide_package_.underdocs.renderer.parser.section.ExcerptParser()
    private val descriptionParser = TextSectionParser("Description")
    private val attributesParser = _root_ide_package_.underdocs.renderer.parser.section.AttributesParser()
    private val examplesParser = _root_ide_package_.underdocs.renderer.parser.section.ExamplesParser()
    private val seeAlsoParser = SeeAlsoParser()
    private val otherParser = _root_ide_package_.underdocs.renderer.parser.section.OtherParser(setOf("Excerpt", "Description", "Attributes", "Examples", "See Also"))

    override fun parse(document: Document) =
            TypeSynonymDocumentation(
                    excerptParser.tryParse(document),
                    descriptionParser.tryParse(document),
                    otherParser.tryParse(document) ?: emptyMap(),
                    examplesParser.tryParse(document) ?: emptyList(),
                    seeAlsoParser.tryParse(document) ?: emptyList(),
                    attributesParser.tryParse(document) ?: emptyMap()
            )
}
