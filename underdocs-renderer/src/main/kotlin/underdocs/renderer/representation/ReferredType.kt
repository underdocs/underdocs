package underdocs.renderer.representation

import underdocs.renderer.representation.visitor.Visitor

data class ReferredType(
        val name: String,
        val specifiers: List<String>
) : Type, Visitable {
    override fun accept(visitor: Visitor) = visitor.accept(this)
}
