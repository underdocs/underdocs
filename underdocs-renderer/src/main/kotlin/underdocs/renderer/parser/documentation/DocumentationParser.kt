package underdocs.renderer.parser.documentation

import com.vladsch.flexmark.util.ast.Document
import underdocs.renderer.representation.documentation.Documentation

interface DocumentationParser<T : Documentation> {
    fun parse(document: Document): T
}
