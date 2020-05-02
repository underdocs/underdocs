package underdocs.renderer.output.html.render.page

import j2html.TagCreator.main
import j2html.tags.Tag
import underdocs.renderer.output.html.render.section.SectionRenderer
import underdocs.renderer.representation.Module

class ModuleRenderer(private val sectionRenderer: SectionRenderer) {
    fun render(module: Module) = content(module)

    private fun content(module: Module): Tag<*> {
        val title = module.documentation?.title ?: module.path

        return main(
                sectionRenderer.renderTitle(title, emptyMap()),
                sectionRenderer.renderSubmodules(module),
                sectionRenderer.renderSubheaders(module)
        )
    }
}