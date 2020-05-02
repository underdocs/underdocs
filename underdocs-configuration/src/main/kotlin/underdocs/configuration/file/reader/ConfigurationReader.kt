package underdocs.configuration.file.reader

interface ConfigurationReader {
    fun readFrom(path: String): String;
}