package underdocs.renderer.representation

import underdocs.renderer.representation.documentation.MemberDocumentation
import underdocs.renderer.representation.visitor.Visitor

data class StructMember(
    private val startingLine: Int,
    private val raw: String,

    val name: String,
    val members: List<Member>,
    val documentation: MemberDocumentation?
) : Element, Member {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun accept(visitor: Visitor) = visitor.accept(this)
}
