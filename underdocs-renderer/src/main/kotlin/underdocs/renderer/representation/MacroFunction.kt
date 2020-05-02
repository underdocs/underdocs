package underdocs.renderer.representation

import underdocs.renderer.representation.documentation.MacroFunctionDocumentation
import underdocs.renderer.representation.visitor.Visitor

data class MacroFunction(
        private val startingLine: Int,
        private val raw: String,

        private var parent: Header?,

        val name: String,
        val expansion: String,
        val parameters: List<String>,
        val documentation: MacroFunctionDocumentation?
) : TopLevelElement {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun getParent() = parent

    override fun accept(visitor: Visitor) = visitor.accept(this)
}
