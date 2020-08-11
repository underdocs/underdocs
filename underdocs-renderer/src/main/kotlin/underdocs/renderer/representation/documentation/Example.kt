package underdocs.renderer.representation.documentation

data class Example(
    val title: String,
    val description: String?,
    val code: String,
    val language: String?,
    val output: String?
)
