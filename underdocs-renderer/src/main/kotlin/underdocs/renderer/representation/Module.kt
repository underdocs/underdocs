package underdocs.renderer.representation

import underdocs.renderer.representation.documentation.ModuleDocumentation
import underdocs.renderer.representation.visitor.Visitor

data class Module(
        val path: String,
        var parent: Module?,

        val documentation: ModuleDocumentation?,

        val headers: List<Header>,
        val children: Map<String, Module>
): Visitable {
    override fun accept(visitor: Visitor) = visitor.accept(this)
}
