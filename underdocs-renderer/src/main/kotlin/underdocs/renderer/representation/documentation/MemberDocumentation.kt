package underdocs.renderer.representation.documentation

data class MemberDocumentation(
        private val attributes: Map<String, String>,
        val description: String?
): Documentation {
    override fun getAttributes() = attributes
}
