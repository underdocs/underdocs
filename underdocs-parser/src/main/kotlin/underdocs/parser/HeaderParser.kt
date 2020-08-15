package underdocs.parser

import underdocs.configuration.ParserConfiguration
import underdocs.representation.Header

interface HeaderParser {
    companion object {
        fun create(configuration: ParserConfiguration): HeaderParser = EclipseHeaderParser(configuration)
    }

    fun parse(path: String): Header
}
