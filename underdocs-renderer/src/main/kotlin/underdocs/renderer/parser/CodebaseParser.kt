package underdocs.renderer.parser

import underdocs.representation.Header
import underdocs.renderer.representation.Module

interface CodebaseParser {
    companion object {
        fun create(): _root_ide_package_.underdocs.renderer.parser.CodebaseParser = _root_ide_package_.underdocs.renderer.parser.CodebaseParserImpl()
    }

    fun parseHeaders(headers: List<Header>): Module
}
