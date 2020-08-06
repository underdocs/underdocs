package underdocs.version

data class Version(val projectVersion: String, val gitCommitHash: String, val gitBranch: String) { }