package underdocs.renderer.representation

import underdocs.renderer.representation.visitor.Visitor

data class StructType(
        private val startingLine: Int,
        private val raw: String,

        val name: String,
        val members: List<Member>
) : Element, Type {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun accept(visitor: Visitor) = visitor.accept(this)
}