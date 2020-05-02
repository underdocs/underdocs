package underdocs.configuration.domain.transient

data class ParserConfiguration(
        var includePath: String?,
        var includingPatterns: List<String>?,
        var excludingPatterns: List<String>?,
        var outputFile: String?
)
