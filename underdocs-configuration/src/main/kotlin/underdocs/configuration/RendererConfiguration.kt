package underdocs.configuration

data class RendererConfiguration(
    val outputDirectory: String,
    val remoteRepositoryLineLinkTemplate: String?,
    val remoteRepositoryTagLinkTemplate: String?,
    val staticResourceDirectory: String?
)
