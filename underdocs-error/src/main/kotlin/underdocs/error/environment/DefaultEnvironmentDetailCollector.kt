package underdocs.error.environment

class DefaultEnvironmentDetailCollector : EnvironmentDetailCollector {
    override fun collect() : String {
        val osDetails = collectOSDetails()
        val underdocsVersion = "1.0.1" // for now it's just built in
        val issueLink = createIssueLink()
        val discordInviteLink = "https://discord.gg/" // link to be completed

        return """
            |$osDetails
            |
            |Underdocs Version: $underdocsVersion
            |
            |Create a new issue on GitHub: $issueLink
            |Join us on Discord: $discordInviteLink
        """.trimMargin()
    }

    private fun collectOSDetails() = """
            |Operating System: ${System.getProperty("os.name")} ${System.getProperty("os.arch")}
            |
            |Java Version: ${System.getProperty("java.version")}
            |Java Version Date: ${System.getProperty("java.version.date")}
            |Java Specification Version: ${System.getProperty("java.specification.version")}
            |Java Vendor: ${System.getProperty("java.vendor")} (${System.getProperty("java.vendor.url")})
            |Java Class Version: ${System.getProperty("java.class.version")}
            |Java Runtime Name: ${System.getProperty("java.runtime.name")}
            |Java Runtime Version: ${System.getProperty("java.runtime.version")}
            |
            |JVM Version: ${System.getProperty("java.vm.version")}
            |JVM Name: ${System.getProperty("java.vm.name")}
            |JVM Vendor: ${System.getProperty("java.vm.vendor")}
            |JVM Specification Version: ${System.getProperty("java.vm.specification.version")}
        """.trimMargin()

    private fun createIssueLink() =
            "https://github.com/underdocs/underdocs/issues/new?assignees=&labels=type%3A+bug+%3Abeetle%3A&title=Bug+Report"
}