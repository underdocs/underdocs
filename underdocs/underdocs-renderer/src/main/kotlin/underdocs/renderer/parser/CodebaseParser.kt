package underdocs.renderer.parser

import underdocs.renderer.representation.Module
import underdocs.representation.Header

interface CodebaseParser {
    companion object {
        fun create(): underdocs.renderer.parser.CodebaseParser = underdocs.renderer.parser.DefaultCodebaseParser()
    }

    fun parseHeaders(headers: List<Header>): Module
}
