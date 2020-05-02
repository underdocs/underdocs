package underdocs.configuration.file.path

import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Paths

class DefaultingConfigurationPathSelector: ConfigurationPathSelector {
    companion object {
        private val DEFAULT_PATHS = listOf(
                "./underdocs.json"
        )
    }

    override fun selectUsingPath(path: String?): String? {
        val paths = mutableListOf(path)
        paths.addAll(DEFAULT_PATHS)

        return paths.find { it != null && Files.exists(Paths.get(it), LinkOption.NOFOLLOW_LINKS) }
    }
}
