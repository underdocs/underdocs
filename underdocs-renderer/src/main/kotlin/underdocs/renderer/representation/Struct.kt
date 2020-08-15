package underdocs.renderer.representation

import underdocs.renderer.representation.documentation.StructDocumentation
import underdocs.renderer.representation.visitor.Visitor

data class Struct(
    private val startingLine: Int,
    private val raw: String,

    private var parent: Header?,

    val name: String,
    val typedef: Boolean,
    val specifiers: List<String>,
    val members: List<Member>,
    val documentation: StructDocumentation?
) : TopLevelElement {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun getParent() = parent

    override fun accept(visitor: Visitor) = visitor.accept(this)

    override fun setParent(header: Header) {
        this.parent = header
    }
}
