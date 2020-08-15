package underdocs.renderer.representation

import underdocs.renderer.representation.documentation.HeaderDocumentation
import underdocs.renderer.representation.visitor.Visitor

data class Header(
    val path: String,
    val filename: String,

    var parent: Module?,

    val elements: Map<String, List<TopLevelElement>>,

    val documentation: HeaderDocumentation?
) : Visitable {
    override fun accept(visitor: Visitor) = visitor.accept(this)
}
