package underdocs.renderer.parser.documentation

import com.vladsch.flexmark.util.ast.Document
import underdocs.renderer.parser.section.AttributesParser
import underdocs.renderer.parser.section.ExamplesParser
import underdocs.renderer.parser.section.ExcerptParser
import underdocs.renderer.parser.section.OtherParser
import underdocs.renderer.parser.section.SeeAlsoParser
import underdocs.renderer.parser.section.TextSectionParser
import underdocs.renderer.representation.documentation.EnumElementDocumentation

class EnumElementDocumentationParser : DocumentationParser<EnumElementDocumentation> {
    private val excerptParser = ExcerptParser()
    private val descriptionParser = TextSectionParser("Description")
    private val attributesParser = AttributesParser()
    private val examplesParser = ExamplesParser()
    private val seeAlsoParser = SeeAlsoParser()
    private val otherParser = OtherParser(setOf("Excerpt", "Description", "Attributes", "Examples", "See Also"))

    override fun parse(document: Document) =
            EnumElementDocumentation(
                    excerptParser.tryParse(document),
                    descriptionParser.tryParse(document),
                    otherParser.tryParse(document) ?: emptyMap(),
                    examplesParser.tryParse(document) ?: emptyList(),
                    seeAlsoParser.tryParse(document) ?: emptyList(),
                    attributesParser.tryParse(document) ?: emptyMap()
            )
}
