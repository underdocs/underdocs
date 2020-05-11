package underdocs.renderer.output.html.render.page

import j2html.TagCreator.div
import j2html.tags.Tag
import underdocs.renderer.output.html.link.Linker
import underdocs.renderer.output.html.render.section.SectionRenderer
import underdocs.renderer.representation.EnumElement
import underdocs.renderer.representation.Function
import underdocs.renderer.representation.MacroConstant
import underdocs.renderer.representation.MacroFunction
import underdocs.renderer.representation.Struct
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.TypeSynonym
import underdocs.renderer.representation.Union
import underdocs.renderer.representation.Variable
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

        sections.add(sectionRenderer.renderMembers(enumElement))

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

    override fun accept(macroFunction: MacroFunction) {
        val sections = ArrayList<Tag<*>>()

        val title = macroFunction.name

        sections.add(sectionRenderer.renderHeading(macroFunction, title, macroFunction.documentation?.getAttributes() ?: emptyMap()))

        sections.add(sectionRenderer.renderRepresentation(macroFunction))

        macroFunction.documentation?.description?.let {
            sections.add(sectionRenderer.renderDescription(it))
        }

        macroFunction.documentation?.parameters?.let { parameters ->
            if (parameters.isNotEmpty()) {
                sections.add(sectionRenderer.renderParameters(macroFunction))
            }
        }

        if (macroFunction.documentation?.examples?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderExamples(macroFunction.documentation.examples))
        }

        if (macroFunction.documentation?.otherSections?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderOtherSections(macroFunction.documentation.otherSections))
        }

        if (macroFunction.documentation?.seeAlso?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderSeeAlso(macroFunction.documentation.seeAlso))
        }

        renderedTag = div().with(sections)
    }

    override fun accept(struct: Struct) {
        val sections = ArrayList<Tag<*>>()

        val title = struct.name ?: "unnamed struct"

        sections.add(sectionRenderer.renderHeading(struct, title, struct.documentation?.getAttributes() ?: emptyMap()))

        sections.add(sectionRenderer.renderRepresentation(struct))

        struct.documentation?.description?.let {
            sections.add(sectionRenderer.renderDescription(it))
        }

        sections.add(sectionRenderer.renderMembers(struct))

        if (struct.documentation?.examples?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderExamples(struct.documentation.examples))
        }

        if (struct.documentation?.otherSections?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderOtherSections(struct.documentation.otherSections))
        }

        if (struct.documentation?.seeAlso?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderSeeAlso(struct.documentation.seeAlso))
        }

        renderedTag = div().with(sections)
    }

    override fun accept(union: Union) {
        val sections = ArrayList<Tag<*>>()

        val title = union.name ?: "unnamed union"

        sections.add(sectionRenderer.renderHeading(union, title, union.documentation?.getAttributes() ?: emptyMap()))

        sections.add(sectionRenderer.renderRepresentation(union))

        union.documentation?.description?.let {
            sections.add(sectionRenderer.renderDescription(it))
        }

        sections.add(sectionRenderer.renderMembers(union))

        if (union.documentation?.examples?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderExamples(union.documentation.examples))
        }

        if (union.documentation?.otherSections?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderOtherSections(union.documentation.otherSections))
        }

        if (union.documentation?.seeAlso?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderSeeAlso(union.documentation.seeAlso))
        }

        renderedTag = div().with(sections)
    }

    override fun accept(function: Function) {
        val sections = ArrayList<Tag<*>>()

        val title = function.name

        sections.add(sectionRenderer.renderHeading(function, title, function.documentation?.getAttributes() ?: emptyMap()))

        sections.add(sectionRenderer.renderRepresentation(function))

        function.documentation?.description?.let {
            sections.add(sectionRenderer.renderDescription(it))
        }

        sections.add(sectionRenderer.renderParameters(function))

        if (function.documentation?.returnValue?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderReturnValue(function, function.documentation.returnValue))
        }

        if (function.documentation?.errorHandling?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderErrorHandling(function.documentation.errorHandling))
        }

        if (function.documentation?.notes?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderNotes(function.documentation.notes))
        }

        if (function.documentation?.examples?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderExamples(function.documentation.examples))
        }

        if (function.documentation?.otherSections?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderOtherSections(function.documentation.otherSections))
        }

        if (function.documentation?.seeAlso?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderSeeAlso(function.documentation.seeAlso))
        }

        renderedTag = div().with(sections)
    }

    override fun accept(variable: Variable) {
        val sections = ArrayList<Tag<*>>()

        val title = variable.name

        sections.add(sectionRenderer.renderHeading(variable, title, variable.documentation?.getAttributes() ?: emptyMap()))

        sections.add(sectionRenderer.renderRepresentation(variable))

        variable.documentation?.description?.let {
            sections.add(sectionRenderer.renderDescription(it))
        }

        if (variable.documentation?.examples?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderExamples(variable.documentation.examples))
        }

        if (variable.documentation?.otherSections?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderOtherSections(variable.documentation.otherSections))
        }

        if (variable.documentation?.seeAlso?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderSeeAlso(variable.documentation.seeAlso))
        }

        renderedTag = div().with(sections)
    }
}