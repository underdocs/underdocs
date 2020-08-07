package underdocs.version.reader

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.util.Properties
import java.util.stream.Collectors

class VersionPropertiesReader: PropertiesReader {
    override fun readProperties(): Properties {
        val properties = Properties()
        val propertiesFile = "underdocs-version.properties"

        val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(propertiesFile)
        val isr = InputStreamReader(inputStream)
        val br = BufferedReader(isr)
        val versionFileContents = br.lines().collect(Collectors.joining("\n"))
        properties.load(StringReader(versionFileContents))

        return properties
    }
}