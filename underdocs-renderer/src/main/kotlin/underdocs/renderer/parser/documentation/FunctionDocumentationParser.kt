package underdocs.renderer.parser.documentation

import com.vladsch.flexmark.util.ast.Document
import underdocs.renderer.parser.section.*
import underdocs.renderer.representation.documentation.FunctionDocumentation

class FunctionDocumentationParser : DocumentationParser<FunctionDocumentation> {
    private val excerptParser = ExcerptParser()
    private val descriptionParser = TextSectionParser("Description")
    private val attributesParser = AttributesParser()
    private val examplesParser = ExamplesParser()
    private val seeAlsoParser = SeeAlsoParser()
    private val returnValueParser = ReturnValueParser()
    private val errorHandlingParser = ErrorHandlingParser()
    private val notesParser = TextSectionParser("Notes")
    private val parametersParser = ParametersParser()
    private val otherParser = OtherParser(setOf("Excerpt", "Description", "Attributes", "Examples", "See Also", "Return Value", "Error Handling", "Notes", "Parameters"))

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
