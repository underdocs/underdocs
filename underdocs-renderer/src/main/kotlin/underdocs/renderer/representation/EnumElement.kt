package underdocs.renderer.representation

import underdocs.renderer.representation.documentation.EnumElementDocumentation
import underdocs.renderer.representation.visitor.Visitor

data class EnumElement(
    private val startingLine: Int,
    private val raw: String,

    private var parent: Header?,

    val name: String?,
    val typedef: Boolean,
    val specifiers: List<String>,
    val members: List<EnumConstant>,

    val documentation: EnumElementDocumentation?
) : TopLevelElement {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun getParent() = parent

    override fun accept(visitor: Visitor) = visitor.accept(this)

    override fun setParent(header: Header) {
        this.parent = header
    }
}
