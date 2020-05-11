package underdocs.renderer.output.html.render.page

import j2html.TagCreator.div
import j2html.tags.Tag
import underdocs.renderer.output.html.link.Linker
import underdocs.renderer.output.html.render.section.SectionRenderer
import underdocs.renderer.representation.MacroConstant
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.visitor.BaseVisitor

class TopLevelElementRenderer(private val linker: Linker, private val sectionRenderer: SectionRenderer): BaseVisitor() {
    private var renderedTag: Tag<*>? = null

    fun render(element: TopLevelElement): Tag<*> {
        element.accept(this)

        return renderedTag!!
    }

    override fun accept(macroConstant: MacroConstant) {
        val sections = ArrayList<Tag<*>>()

        val title = macroConstant.name

        sections.add(sectionRenderer.renderHeading(macroConstant, title, macroConstant.documentation?.getAttributes() ?: emptyMap()))

        sections.add(sectionRenderer.renderRepresentation(macroConstant))

        macroConstant.documentation?.description?.let {
            sections.add(sectionRenderer.renderDescription(it))
        }

        if (macroConstant.documentation?.examples?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderExamples(macroConstant.documentation.examples))
        }

        if (macroConstant.documentation?.otherSections?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderOtherSections(macroConstant.documentation.otherSections))
        }

        if (macroConstant.documentation?.seeAlso?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderSeeAlso(macroConstant.documentation.seeAlso))
        }

        renderedTag = div().with(sections)
    }
}