package underdocs.renderer.representation

import underdocs.renderer.representation.documentation.FunctionDocumentation
import underdocs.renderer.representation.visitor.Visitor

data class Function(
        private val startingLine: Int,
        private val raw: String,

        private var parent: Header?,

        val name: String,
        val specifiers: List<String>,
        val returnType: ReferredType,
        val parameters: List<FunctionParameter>,
        val documentation: FunctionDocumentation?
) : TopLevelElement {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun getParent() = parent

    override fun accept(visitor: Visitor) = visitor.accept(this)

    override fun setParent(header: Header) {
        this.parent = header
    }
}
