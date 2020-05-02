package underdocs.representation

import underdocs.representation.visitor.ElementVisitor

data class StructType(
        private val startingLine: Int,
        private val raw: String,

        val name: String,
        val members: List<Member>
) : Element, Type {
    override fun getStartingLine() = startingLine

    override fun getRaw() = raw

    override fun accept(visitor: ElementVisitor) = visitor.visit(this)
}