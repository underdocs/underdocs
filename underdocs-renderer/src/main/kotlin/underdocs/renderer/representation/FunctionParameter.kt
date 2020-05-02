package underdocs.renderer.representation

import underdocs.renderer.representation.visitor.Visitor

data class FunctionParameter(
        private val startingLine: Int,
        private val raw: String,

        val name: String,
        val type: ReferredType
) : Element {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun accept(visitor: Visitor) = visitor.accept(this)
}
