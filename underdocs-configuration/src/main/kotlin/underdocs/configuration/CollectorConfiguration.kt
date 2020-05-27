package underdocs.configuration

data class CollectorConfiguration(
        val includePath: String,
        val includingPatterns: List<String>,
        val excludingPatterns: List<String>
)
