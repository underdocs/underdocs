package underdocs.renderer.representation.documentation

data class ModuleDocumentation(
        val title: String?,
        val excerpt: String?,
        val description: String?,
        val otherSections: Map<String, String>,
        val examples: List<Example>,
        val seeAlso: List<String>,
        private val attributes: Map<String, String>
) : Documentation {
    override fun getAttributes() = attributes
}
