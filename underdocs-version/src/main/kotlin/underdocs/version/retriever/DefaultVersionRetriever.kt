package underdocs.version.retriever

import underdocs.version.Version
import underdocs.version.reader.VersionPropertiesReader

class DefaultVersionRetriever: VersionRetriever {
    override fun get(): Version {
        val propertiesReader = VersionPropertiesReader()
        val versionProperties = propertiesReader.readProperties()

        val projectVersion = versionProperties.getProperty("project.version")
        val gitCommitHash = versionProperties.getProperty("git.commit.hash")
        val gitBranch = versionProperties.getProperty("git.branch")

        return Version(projectVersion, gitCommitHash, gitBranch)
    }
}