package underdocs.representation

import underdocs.representation.visitor.ElementVisitor

data class Function(
    private val startingLine: Int,
    private val raw: String,
    val name: String,
    val specifiers: List<String>,
    val returnType: ReferredType,
    val parameters: List<FunctionParameter>,
    val comment: String?
) : Element {
    override fun getStartingLine() = startingLine

    override fun getRaw() = raw

    override fun accept(visitor: ElementVisitor) = visitor.visit(this)
}
