package underdocs.cli.common

import underdocs.configuration.domain.Configuration
import underdocs.configuration.file.loader.ExtensionBasedConfigurationLoader
import underdocs.configuration.file.path.DefaultingConfigurationPathSelector
import underdocs.configuration.file.reader.FileConfigurationReader
import java.nio.file.Path

fun configurationFromPath(configurationPath: Path?): Configuration {
    val loader = ExtensionBasedConfigurationLoader(
            DefaultingConfigurationPathSelector(),
            FileConfigurationReader()
    )

    loader.loadFrom(configurationPath.toString())
}
