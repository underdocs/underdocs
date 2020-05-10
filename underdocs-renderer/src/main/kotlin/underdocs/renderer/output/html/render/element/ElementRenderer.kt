package underdocs.renderer.output.html.render.element

import j2html.tags.Tag
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.TypeSynonym
import underdocs.renderer.representation.visitor.BaseVisitor

class ElementRenderer(private val sectionRenderer: underdocs.renderer.output.html.render.section.SectionRenderer) : BaseVisitor() {
    private var renderedTag: Tag<*>? = null

    private val typeSynonymRenderer = underdocs.renderer.output.html.render.element.TypeSynonymRenderer()

    fun render(element: TopLevelElement): Tag<*> {
        element.accept(this)

        return renderedTag!!
    }

    override fun accept(typeSynonym: TypeSynonym) {
        renderedTag = typeSynonymRenderer.render(typeSynonym)
    }
}
