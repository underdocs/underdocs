package underdocs.configuration.file.mismatch

class InvalidConfigurationException(private val mismatches: List<ConfigurationMismatch>): Exception() {
    fun getMismatches(): List<ConfigurationMismatch> = mismatches
}
