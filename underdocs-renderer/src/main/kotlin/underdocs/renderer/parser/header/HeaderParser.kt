package underdocs.renderer.parser.header

import underdocs.renderer.representation.Header
import underdocs.representation.Header as CommonHeader

interface HeaderParser {
    companion object {
        fun create(): underdocs.renderer.parser.header.HeaderParser =
            underdocs.renderer.parser.header.DefaultHeaderParser(underdocs.renderer.parser.markdown.MarkdownParserProvider.provide())
    }

    fun parse(header: CommonHeader): Header
}
