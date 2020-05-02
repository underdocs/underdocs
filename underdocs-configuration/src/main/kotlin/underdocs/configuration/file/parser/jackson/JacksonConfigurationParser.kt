package underdocs.configuration.file.parser.jackson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import underdocs.configuration.domain.Configuration
import underdocs.configuration.file.parser.ConfigurationParser

class JacksonConfigurationParser: ConfigurationParser {
    private val mapper = ObjectMapper()
            .registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun parse(configurationString: String) =
            mapper.readValue<Configuration>(configurationString, Configuration::class.java)
}