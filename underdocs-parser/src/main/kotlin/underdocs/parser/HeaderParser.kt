package underdocs.parser

import underdocs.parser.configuration.EclipseParserConfiguration
import underdocs.representation.Header

interface HeaderParser {
    companion object {
        fun create(configuration: EclipseParserConfiguration): HeaderParser = EclipseHeaderParser(configuration)
    }

    fun parse(path: String): Header
}