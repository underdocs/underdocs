package underdocs.representation

import underdocs.representation.visitor.ElementVisitor

data class TypeSynonym(
    private val startingLine: Int,
    private val raw: String,

    val originalName: String,
    val newName: String,
    val comment: String?
) : Element {
    override fun getStartingLine() = startingLine

    override fun getRaw() = raw

    override fun accept(visitor: ElementVisitor) = visitor.visit(this)
}
