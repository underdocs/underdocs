package underdocs.version.retriever

import underdocs.version.Version

interface VersionRetriever {
    companion object {
        fun create(): VersionRetriever = DefaultVersionRetriever()
    }

    fun get(): Version
}