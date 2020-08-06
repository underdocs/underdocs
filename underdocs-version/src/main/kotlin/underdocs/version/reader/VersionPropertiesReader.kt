package underdocs.version.reader

import java.io.FileInputStream
import java.util.Properties

class VersionPropertiesReader: PropertiesReader {
    override fun readProperties(): Properties {
        val properties = Properties()
        val propertiesFile = "\\underdocs-version.properties"

        val inputStream = FileInputStream(propertiesFile)
        properties.load(inputStream)

        return properties
    }
}