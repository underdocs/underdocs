package underdocs.renderer.representation

import underdocs.renderer.representation.documentation.VariableDocumentation
import underdocs.renderer.representation.visitor.Visitor

data class Variable(
        private val startingLine: Int,
        private val raw: String,

        private var parent: Header?,

        val name: String,
        val specifiers: List<String>,
        val type: Type,

        val documentation: VariableDocumentation?
): TopLevelElement, Visitable {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun getParent() = parent

    override fun accept(visitor: Visitor) = visitor.accept(this)
}