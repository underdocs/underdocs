package underdocs.configuration.file.reader

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

class FileConfigurationReader: ConfigurationReader {
    override fun readFrom(path: String): String {
        val contents = Files.readAllBytes(Paths.get(path))

        return String(contents, Charset.defaultCharset())
    }
}