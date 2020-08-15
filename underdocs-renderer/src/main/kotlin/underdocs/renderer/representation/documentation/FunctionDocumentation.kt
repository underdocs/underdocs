package underdocs.renderer.representation.documentation

data class FunctionDocumentation(
    val excerpt: String?,
    val description: String?,
    val otherSections: Map<String, String>,
    val examples: List<Example>,
    val seeAlso: List<String>,
    private val attributes: Map<String, String>,
    val returnValue: ReturnValue?,
    val parameters: Map<String, String>,
    val errorHandling: ErrorHandling?,
    val notes: String?
) : Documentation {
    override fun getAttributes() = attributes
}
