package underdocs.renderer.output.html.render.page

import j2html.TagCreator.body
import j2html.TagCreator.head
import j2html.TagCreator.html
import j2html.TagCreator.main
import j2html.tags.Tag
import underdocs.renderer.representation.Module

class ModuleRenderer(private val sectionRenderer: _root_ide_package_.underdocs.renderer.output.html.render.section.SectionRenderer) {
    fun render(module: Module) = html(
            head(

            ),
            body(
                    content(module)
            )
    )

    private fun content(module: Module): Tag<*> {
        val title = module.documentation?.title ?: module.path

        return main(
                sectionRenderer.renderTitle(title, emptyMap())
        )
    }
}