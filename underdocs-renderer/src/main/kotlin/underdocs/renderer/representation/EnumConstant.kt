package underdocs.renderer.representation

import underdocs.renderer.representation.documentation.MemberDocumentation
import underdocs.renderer.representation.visitor.Visitor

data class EnumConstant(
        private var startingLine: Int,
        private var raw: String,

        var name: String,
        var value: String?,
        var documentation: MemberDocumentation?
) : Element {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun accept(visitor: Visitor) = visitor.accept(this)
}
