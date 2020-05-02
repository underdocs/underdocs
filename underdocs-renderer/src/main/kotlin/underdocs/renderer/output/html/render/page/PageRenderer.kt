package underdocs.renderer.output.html.render.page

import j2html.TagCreator.body
import j2html.TagCreator.head
import j2html.TagCreator.html
import j2html.tags.Tag
import underdocs.renderer.output.html.render.section.SectionRenderer
import underdocs.renderer.representation.Header
import underdocs.renderer.representation.Module
import underdocs.renderer.representation.TopLevelElement

class PageRenderer(private val sectionRenderer: SectionRenderer) {
    private val moduleRenderer = ModuleRenderer(sectionRenderer)

    fun render(element: TopLevelElement): Tag<*> {
        TODO()
    }

    fun render(header: Header): Tag<*> {
        TODO()
    }

    fun render(module: Module) = page(
            moduleRenderer.render(module)
    )

    private fun page(contents: Tag<*>) = html(
            head(),
            body(
                    contents
            )
    )
}
