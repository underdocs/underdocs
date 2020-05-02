package underdocs.renderer.parser.documentation

import com.vladsch.flexmark.util.ast.Document
import underdocs.renderer.parser.section.SeeAlsoParser
import underdocs.renderer.parser.section.TextSectionParser
import underdocs.renderer.representation.documentation.FunctionDocumentation

class FunctionDocumentationParser: _root_ide_package_.underdocs.renderer.parser.documentation.DocumentationParser<FunctionDocumentation> {
    private val excerptParser = _root_ide_package_.underdocs.renderer.parser.section.ExcerptParser()
    private val descriptionParser = TextSectionParser("Description")
    private val attributesParser = _root_ide_package_.underdocs.renderer.parser.section.AttributesParser()
    private val examplesParser = _root_ide_package_.underdocs.renderer.parser.section.ExamplesParser()
    private val seeAlsoParser = SeeAlsoParser()
    private val returnValueParser = TextSectionParser("Return Value")
    private val errorHandlingParser = TextSectionParser("Error Handling")
    private val notesParser = TextSectionParser("Notes")
    private val parametersParser = _root_ide_package_.underdocs.renderer.parser.section.ParametersParser()
    private val otherParser = _root_ide_package_.underdocs.renderer.parser.section.OtherParser(setOf("Excerpt", "Description", "Attributes", "Examples", "See Also", "Return Value", "Error Handling", "Notes", "Parameters"))

    override fun parse(document: Document) =
            FunctionDocumentation(
                    excerptParser.tryParse(document),
                    descriptionParser.tryParse(document),
                    otherParser.tryParse(document) ?: emptyMap(),
                    examplesParser.tryParse(document) ?: emptyList(),
                    seeAlsoParser.tryParse(document) ?: emptyList(),
                    attributesParser.tryParse(document) ?: emptyMap(),
                    returnValueParser.tryParse(document),
                    parametersParser.tryParse(document) ?: emptyMap(),
                    errorHandlingParser.tryParse(document),
                    notesParser.tryParse(document)
            )
}
