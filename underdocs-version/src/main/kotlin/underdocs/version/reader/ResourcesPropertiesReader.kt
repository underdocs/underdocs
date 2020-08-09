package underdocs.version.reader

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.util.Properties
import java.util.stream.Collectors

class ResourcesPropertiesReader: PropertiesReader {
    override fun readProperties(from: String): Properties {
        val properties = Properties()
        var resourceFileContents = ""

        val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(from)
        inputStream.use {
            val isr = InputStreamReader(inputStream)
            isr.use {
                val br = BufferedReader(isr)
                resourceFileContents = br.lines().collect(Collectors.joining("\n"))
            }
        }

        properties.load(StringReader(resourceFileContents))

        return properties
    }
}