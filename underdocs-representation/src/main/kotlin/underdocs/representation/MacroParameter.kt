package underdocs.representation

import underdocs.representation.visitor.ElementVisitor

data class MacroParameter(
        private val startingLine: Int,
        private val raw: String,

        val name: String
) : Element {
    override fun getStartingLine() = startingLine

    override fun getRaw() = raw

    override fun accept(visitor: ElementVisitor) = visitor.visit(this)
}
