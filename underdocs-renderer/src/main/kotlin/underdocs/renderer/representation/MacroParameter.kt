package underdocs.renderer.representation

import underdocs.renderer.representation.visitor.Visitor

data class MacroParameter(
        private val startingLine: Int,
        private val raw: String,

        val name: String
) : Element {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun accept(visitor: Visitor) = visitor.accept(this)
}
