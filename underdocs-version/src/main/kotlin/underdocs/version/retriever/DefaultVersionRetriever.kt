package underdocs.version.retriever

import underdocs.version.Version
import underdocs.version.reader.VersionPropertiesReader
import java.util.*

class DefaultVersionRetriever: VersionRetriever {
    override fun get(): Version {
        val versionProperties : Properties by lazy {
            val propertiesReader = VersionPropertiesReader()
            propertiesReader.readProperties()
        }

        val projectVersion = versionProperties.getProperty("project.version")
        val gitCommitHash = versionProperties.getProperty("git.commit.hash")
        val gitBranch = versionProperties.getProperty("git.branch")

        return Version(projectVersion, gitCommitHash, gitBranch)
    }
}