package underdocs.configuration.file.mismatch

data class ConfigurationMismatch(
    val key: String,
    val actualValue: String,
    val expected: String
)
