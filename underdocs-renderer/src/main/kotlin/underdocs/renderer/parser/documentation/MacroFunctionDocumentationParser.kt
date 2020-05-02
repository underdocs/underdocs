package underdocs.renderer.parser.documentation

import com.vladsch.flexmark.util.ast.Document
import underdocs.renderer.parser.section.SeeAlsoParser
import underdocs.renderer.parser.section.TextSectionParser
import underdocs.renderer.representation.documentation.MacroFunctionDocumentation

class MacroFunctionDocumentationParser: underdocs.renderer.parser.documentation.DocumentationParser<MacroFunctionDocumentation> {
    private val excerptParser = underdocs.renderer.parser.section.ExcerptParser()
    private val descriptionParser = TextSectionParser("Description")
    private val attributesParser = underdocs.renderer.parser.section.AttributesParser()
    private val examplesParser = underdocs.renderer.parser.section.ExamplesParser()
    private val seeAlsoParser = SeeAlsoParser()
    private val parametersParser = underdocs.renderer.parser.section.ParametersParser()
    private val otherParser = underdocs.renderer.parser.section.OtherParser(setOf("Excerpt", "Description", "Attributes", "Examples", "See Also", "Parameters"))

    override fun parse(document: Document) =
            MacroFunctionDocumentation(
                    excerptParser.tryParse(document),
                    descriptionParser.tryParse(document),
                    otherParser.tryParse(document) ?: emptyMap(),
                    examplesParser.tryParse(document) ?: emptyList(),
                    seeAlsoParser.tryParse(document) ?: emptyList(),
                    attributesParser.tryParse(document) ?: emptyMap(),
                    parametersParser.tryParse(document) ?: emptyMap()
            )
}