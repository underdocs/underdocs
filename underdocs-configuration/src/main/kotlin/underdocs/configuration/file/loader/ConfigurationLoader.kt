package underdocs.configuration.file.loader

import underdocs.configuration.domain.Configuration

interface ConfigurationLoader {
    fun loadFrom(path: String?): Configuration;
}