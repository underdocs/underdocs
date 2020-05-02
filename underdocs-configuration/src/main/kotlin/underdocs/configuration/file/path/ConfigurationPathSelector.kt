package underdocs.configuration.file.path

interface ConfigurationPathSelector {
    fun selectUsingPath(path: String?): String?
}

