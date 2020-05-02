package underdocs.renderer.parser.header

import underdocs.renderer.representation.Header
import underdocs.representation.Header as CommonHeader

interface HeaderParser {
    companion object {
        fun create(): _root_ide_package_.underdocs.renderer.parser.header.HeaderParser =
                _root_ide_package_.underdocs.renderer.parser.header.HeaderParserImpl(_root_ide_package_.underdocs.renderer.parser.markdown.MarkdownParserProvider.provide())
    }

    fun parse(header: CommonHeader): Header
}
