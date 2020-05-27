package underdocs.renderer.representation.documentation

data class HeaderDocumentation(
        val excerpt: String?,
        val description: String?,
        val otherSections: Map<String, String>,
        val examples: List<Example>,
        val seeAlso: List<String>,
        private val attributes: Map<String, String>,
        val groups: Map<String, String>
) : Documentation {
    override fun getAttributes() = attributes
}
