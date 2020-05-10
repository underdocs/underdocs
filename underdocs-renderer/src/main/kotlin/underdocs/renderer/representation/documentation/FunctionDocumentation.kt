package underdocs.renderer.representation.documentation

data class FunctionDocumentation(
        val excerpt: String?,
        val description: String?,
        val otherSections: Map<String, String>,
        val examples: List<Example>,
        val seeAlso: List<String>,
        private val attributes: Map<String, String>,
        val returnValue: String?,
        val parameters: Map<String, String>,
        val errorHandling: String?,
        val notes: String?
) : Documentation {
    override fun getAttributes() = attributes
}
