package underdocs.representation

import underdocs.representation.visitor.ElementVisitor

data class Variable(
        private val startingLine: Int,
        private val raw: String,

        val name: String,
        val specifiers: List<String>,
        val type: Type,

        val comment: String?
) : Element {
    override fun getStartingLine() = startingLine

    override fun getRaw() = raw

    override fun accept(visitor: ElementVisitor) = visitor.visit(this)
}