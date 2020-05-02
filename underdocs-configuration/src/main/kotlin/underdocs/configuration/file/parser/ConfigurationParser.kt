package underdocs.configuration.file.parser

import underdocs.configuration.domain.Configuration

interface ConfigurationParser {
    fun parse(configurationString: String): Configuration
}
