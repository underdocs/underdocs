package underdocs.renderer.representation

import underdocs.renderer.representation.documentation.MacroConstantDocumentation
import underdocs.renderer.representation.visitor.Visitor

data class MacroConstant(
    private val startingLine: Int,
    private val raw: String,

    private var parent: Header?,

    val name: String,
    val expansion: String,
    val documentation: MacroConstantDocumentation?
) : TopLevelElement {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun getParent() = parent

    override fun accept(visitor: Visitor) = visitor.accept(this)

    override fun setParent(header: Header) {
        this.parent = header
    }
}
