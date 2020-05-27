package underdocs.representation

import underdocs.representation.visitor.ElementVisitor

data class MacroFunction(
        private val startingLine: Int,
        private val raw: String,

        val name: String,
        val expansion: String,
        val parameters: List<String>,
        val comment: String?
) : Element {
    override fun getStartingLine() = startingLine

    override fun getRaw() = raw

    override fun accept(visitor: ElementVisitor) = visitor.visit(this)
}