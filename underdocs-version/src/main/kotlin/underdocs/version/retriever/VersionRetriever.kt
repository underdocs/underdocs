package underdocs.version.retriever

import underdocs.version.Version
import underdocs.version.reader.ResourcesPropertiesReader

interface VersionRetriever {
    companion object {
        fun create(): VersionRetriever = DefaultVersionRetriever(ResourcesPropertiesReader())
    }

    fun get(): Version
}
