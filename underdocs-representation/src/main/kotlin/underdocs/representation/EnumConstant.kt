package underdocs.representation

import underdocs.representation.visitor.ElementVisitor

data class EnumConstant(
        private var startingLine: Int,
        private var raw: String,

        var name: String,
        var value: String?,
        var comment: String?
) : Element {
    override fun getStartingLine() = startingLine

    override fun getRaw() = raw

    override fun accept(visitor: ElementVisitor) = visitor.visit(this)
}
