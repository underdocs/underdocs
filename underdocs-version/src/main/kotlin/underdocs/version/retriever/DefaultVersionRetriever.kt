package underdocs.version.retriever

import underdocs.version.Version
import underdocs.version.reader.PropertiesReader
import java.util.*

class DefaultVersionRetriever(private val propertiesReader: PropertiesReader): VersionRetriever {
    private val versionProperties : Properties by lazy {
        propertiesReader.readProperties("underdocs-version.properties")
    }

    override fun get(): Version {
        val projectVersion = versionProperties.getProperty("project.version")
        val gitCommitHash = versionProperties.getProperty("git.commit.hash")
        val gitBranch = versionProperties.getProperty("git.branch")

        return Version(projectVersion, gitCommitHash, gitBranch)
    }
}