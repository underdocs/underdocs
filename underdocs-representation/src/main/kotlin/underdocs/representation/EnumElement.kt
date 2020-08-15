package underdocs.representation

import underdocs.representation.visitor.ElementVisitor

data class EnumElement(
    private val startingLine: Int,
    private val raw: String,
    val name: String?,
    val typedef: Boolean,
    val specifiers: List<String>,
    val members: List<EnumConstant>,

    val comment: String?
) : Element {
    override fun getStartingLine() = startingLine

    override fun getRaw() = raw

    override fun accept(visitor: ElementVisitor) = visitor.visit(this)
}
