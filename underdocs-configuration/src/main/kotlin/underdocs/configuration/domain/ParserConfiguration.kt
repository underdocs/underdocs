package underdocs.configuration.domain

data class ParserConfiguration(
        val includePath: String,
        val includingPatterns: List<String>,
        val excludingPatterns: List<String>,
        val outputFile: String
)
