package underdocs.configuration.file.loader

import underdocs.configuration.domain.Configuration
import underdocs.configuration.file.parser.ConfigurationParser
import underdocs.configuration.file.parser.jackson.JacksonConfigurationParser
import underdocs.configuration.file.path.ConfigurationPathSelector
import underdocs.configuration.file.reader.ConfigurationReader
import java.lang.IllegalStateException

class ExtensionBasedConfigurationLoader(private val pathSelector: ConfigurationPathSelector,
                                        private val reader: ConfigurationReader): ConfigurationLoader {
    companion object {
        private val KNOWN_EXTENSIONS = setOf(
                "json"
        )
    }

    override fun loadFrom(path: String?): Configuration {
        val actualPath = selectActualConfigurationPath(path)

        val parser = selectParserBasedOnPath(actualPath)

        val configurationString = reader.readFrom(actualPath)

        return parser.parse(configurationString)
    }

    private fun selectActualConfigurationPath(providedPath: String?): String {
        val selectedPath = pathSelector.selectUsingPath(providedPath) ?: throw IllegalStateException()

        val extension = getExtension(selectedPath)

        if (extension !in KNOWN_EXTENSIONS) {
            throw IllegalStateException()
        }

        return selectedPath
    }

    private fun selectParserBasedOnPath(path: String?): ConfigurationParser {
        val selectedPath = pathSelector.selectUsingPath(path) ?: throw IllegalStateException()

        val extension = getExtension(selectedPath)

        if (extension !in KNOWN_EXTENSIONS) {
            throw IllegalStateException()
        }

        return selectPaserBasedOnExtension(extension)
    }

    private fun getExtension(path: String) =
        path.split('.').last()

    private fun selectPaserBasedOnExtension(extension: String) = when (extension) {
        "json" -> JacksonConfigurationParser()
        else -> throw IllegalStateException("F")
    }
}
