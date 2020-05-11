package underdocs.renderer.output.html.render.page

import j2html.TagCreator.div
import j2html.tags.Tag
import underdocs.renderer.output.html.link.Linker
import underdocs.renderer.output.html.render.section.SectionRenderer
import underdocs.renderer.representation.EnumElement
import underdocs.renderer.representation.MacroConstant
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.TypeSynonym
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

    override fun accept(typeSynonym: TypeSynonym) {
        val sections = ArrayList<Tag<*>>()

        val title = typeSynonym.newName

        sections.add(sectionRenderer.renderHeading(typeSynonym, title, typeSynonym.documentation?.getAttributes() ?: emptyMap()))

        sections.add(sectionRenderer.renderRepresentation(typeSynonym))

        typeSynonym.documentation?.description?.let {
            sections.add(sectionRenderer.renderDescription(it))
        }

        if (typeSynonym.documentation?.examples?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderExamples(typeSynonym.documentation.examples))
        }

        if (typeSynonym.documentation?.otherSections?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderOtherSections(typeSynonym.documentation.otherSections))
        }

        if (typeSynonym.documentation?.seeAlso?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderSeeAlso(typeSynonym.documentation.seeAlso))
        }

        renderedTag = div().with(sections)
    }

    override fun accept(enumElement: EnumElement) {
        val sections = ArrayList<Tag<*>>()

        val title = enumElement.name ?: "unnamed enum"

        sections.add(sectionRenderer.renderHeading(enumElement, title, enumElement.documentation?.getAttributes() ?: emptyMap()))

        sections.add(sectionRenderer.renderRepresentation(enumElement))

        enumElement.documentation?.description?.let {
            sections.add(sectionRenderer.renderDescription(it))
        }

        if (enumElement.documentation?.examples?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderExamples(enumElement.documentation.examples))
        }

        if (enumElement.documentation?.otherSections?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderOtherSections(enumElement.documentation.otherSections))
        }

        if (enumElement.documentation?.seeAlso?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderSeeAlso(enumElement.documentation.seeAlso))
        }

        renderedTag = div().with(sections)
    }
}