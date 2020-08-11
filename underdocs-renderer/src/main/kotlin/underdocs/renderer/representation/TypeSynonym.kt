package underdocs.renderer.representation

import underdocs.renderer.representation.documentation.TypeSynonymDocumentation
import underdocs.renderer.representation.visitor.Visitor

data class TypeSynonym(
    private val startingLine: Int,
    private val raw: String,

    private var parent: Header?,

    val originalName: String,
    val newName: String,
    val documentation: TypeSynonymDocumentation?
) : TopLevelElement {
    override fun getStartingLine() = startingLine

    override fun getRawRepresentation() = raw

    override fun getParent() = parent

    override fun accept(visitor: Visitor) = visitor.accept(this)

    override fun setParent(header: Header) {
        this.parent = header
    }
}
