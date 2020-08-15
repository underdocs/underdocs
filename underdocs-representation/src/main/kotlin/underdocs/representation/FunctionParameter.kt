package underdocs.representation

import underdocs.representation.visitor.ElementVisitor

data class FunctionParameter(
    private val startingLine: Int,
    private val raw: String,

    val name: String,
    val type: ReferredType
) : Element {
    override fun getStartingLine() = startingLine

    override fun getRaw() = raw

    override fun accept(visitor: ElementVisitor) = visitor.visit(this)
}
