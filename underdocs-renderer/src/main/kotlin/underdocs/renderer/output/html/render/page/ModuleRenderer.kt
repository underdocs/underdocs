package underdocs.renderer.output.html.render.page

import j2html.TagCreator.div
import j2html.tags.Tag
import underdocs.renderer.output.html.render.section.SectionRenderer
import underdocs.renderer.representation.Module
import java.io.File

class ModuleRenderer(private val sectionRenderer: SectionRenderer) {
    fun render(module: Module) = content(module)

    private fun content(module: Module): Tag<*> {
        val sections = ArrayList<Tag<*>>()

        val title = module.documentation?.title ?: module.path.split(File.separator).last()

        sections.add(sectionRenderer.renderHeading(module, title, module.documentation?.getAttributes() ?: emptyMap()))

        module.documentation?.description?.let {
            sections.add(sectionRenderer.renderDescription(it))
        }

        if (module.children.isNotEmpty()) {
            sections.add(sectionRenderer.renderSubmodules(module))
        }

        if (module.headers.isNotEmpty()) {
            sections.add(sectionRenderer.renderSubheaders(module))
        }

        if (module.documentation?.examples?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderExamples(module.documentation.examples))
        }

        if (module.documentation?.otherSections?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderOtherSections(module.documentation.otherSections))
        }

        if (module.documentation?.seeAlso?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderSeeAlso(module.documentation.seeAlso))
        }

        return div().with(sections)
    }
}