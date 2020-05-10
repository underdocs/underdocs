package underdocs.renderer.output.html.render.page

import j2html.tags.Tag
import underdocs.renderer.output.html.link.Linker
import underdocs.renderer.output.html.render.section.SectionRenderer
import underdocs.renderer.representation.TopLevelElement

class TopLevelElementRenderer(private val linker: Linker, private val sectionRenderer: SectionRenderer) {
    fun render(element: TopLevelElement): Tag<*> {
        TODO()
    }
}