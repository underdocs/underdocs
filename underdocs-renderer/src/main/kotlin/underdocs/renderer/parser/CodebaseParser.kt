package underdocs.renderer.parser

import underdocs.representation.Header
import underdocs.renderer.representation.Module

interface CodebaseParser {
    companion object {
        fun create(): underdocs.renderer.parser.CodebaseParser = underdocs.renderer.parser.DefaultCodebaseParser()
    }

    fun parseHeaders(headers: List<Header>): Module
}
