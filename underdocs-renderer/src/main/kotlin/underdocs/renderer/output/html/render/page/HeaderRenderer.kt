package underdocs.renderer.output.html.render.page

import j2html.TagCreator.a
import j2html.TagCreator.div
import j2html.TagCreator.each
import j2html.TagCreator.h2
import j2html.TagCreator.h3
import j2html.TagCreator.section
import j2html.TagCreator.table
import j2html.TagCreator.tbody
import j2html.TagCreator.td
import j2html.TagCreator.tr
import j2html.tags.DomContent
import j2html.tags.Tag
import j2html.tags.Text
import underdocs.renderer.output.html.link.Linker
import underdocs.renderer.output.html.render.section.SectionRenderer
import underdocs.renderer.representation.EnumElement
import underdocs.renderer.representation.Function
import underdocs.renderer.representation.Header
import underdocs.renderer.representation.MacroConstant
import underdocs.renderer.representation.MacroFunction
import underdocs.renderer.representation.Struct
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.TypeSynonym
import underdocs.renderer.representation.Union
import underdocs.renderer.representation.Variable

class HeaderRenderer(private val linker: Linker, private val sectionRenderer: SectionRenderer) {
    fun render(header: Header) = content(header)

    private fun content(header: Header): Tag<*> {
        val sections = ArrayList<Tag<*>>()

        val title = header.filename

        sections.add(sectionRenderer.renderHeading(header, title, header.documentation?.getAttributes() ?: emptyMap()))

        header.documentation?.description?.let {
            sections.add(sectionRenderer.renderDescription(it))
        }

        sections.add(elements(header))

        if (header.documentation?.examples?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderExamples(header.documentation.examples))
        }

        if (header.documentation?.otherSections?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderOtherSections(header.documentation.otherSections))
        }

        if (header.documentation?.seeAlso?.isNotEmpty() == true) {
            sections.add(sectionRenderer.renderSeeAlso(header.documentation.seeAlso))
        }

        return div().with(sections)
    }

    private fun elements(header: Header): Tag<*> {
        val groupMap = HashMap<String, String?>(header.documentation?.groups ?: emptyMap())

        header.elements
                .filter { (key, _) -> key !in groupMap }
                .forEach { (key, _) -> groupMap[key] = null }

        if (groupMap.isEmpty()) {
            return section().withClass("elements")
        }

        if (groupMap.size == 1 && groupMap.containsKey("UNKNOWN")) {
            return section(
                    div()
                            .with(elementsInGroup(header.elements.get("UNKNOWN") ?: emptyList()))
                            .withClass("element-group")
            ).withClass("elements")
        }

        val orderedGroups = groupMap.entries
                .filter { (key, _) -> key != "UNKNOWN" }
                .map { (name, desc) -> HeaderGroup(name, desc) }
                .sortedBy { (name, _) -> name }
                .toMutableList()

        if ("UNKNOWN" in groupMap) {
            orderedGroups.add(0, HeaderGroup("UNKNOWN", null))
        }

        return section(
                each(orderedGroups) { (name, description) ->
                    val contents = ArrayList<DomContent>()

                    if (name != "UNKNOWN") {
                        contents.add(h2(name))
                    }

                    description?.let {
                        contents.add(sectionRenderer.renderDescription(it))
                    }

                    header.elements.get(name)
                            ?.let { elementsInGroup(it) }
                            ?.let { contents.add(it) }

                    div().with(contents).withClass("element-group")
                }
        ).withClass("elements")
    }

    private fun elementsInGroup(elements: List<TopLevelElement>): Tag<*>? {
        val typeGroups = listOf(
                tryRenderElementsOfTypes("Constants", elements, listOf(MacroConstant::class.java)),
                tryRenderElementsOfTypes("Types", elements, listOf(Struct::class.java, Union::class.java, TypeSynonym::class.java, EnumElement::class.java)),
                tryRenderElementsOfTypes("Functions", elements, listOf(Function::class.java, MacroFunction::class.java)),
                tryRenderElementsOfTypes("Variables", elements, listOf(Variable::class.java))
        ).filterNotNull()

        if (typeGroups.isEmpty()) {
            return null
        }

        return div().with(typeGroups)
    }

    private fun tryRenderElementsOfTypes(title: String, elements: List<TopLevelElement>, types: List<Class<*>>): Tag<*>? {
        val matchingElements = elements.filter { element ->
            types.any { type -> element.javaClass == type }
        }

        if (matchingElements.isEmpty()) {
            return null
        }

        val orderedElements = matchingElements.sortedBy { nameForElement(it) }

        return div(
                h3(title),
                table(
                        tbody(
                                each(orderedElements) { element ->
                                    val name = nameForElement(element)
                                    val excerpt = excerptForElement(element)

                                    val excerptContents = ArrayList<DomContent>()

                                    excerptContents.add(div(
                                            excerpt?.let { sectionRenderer.renderMarkdown(it) } ?: Text("")
                                    ).withClass("element-excerpt-text"))

                                    if (types.size > 1) {
                                        excerptContents.add(div(
                                                "(${getHumanReadableTypeName(element)})"
                                        ).withClass("element-excerpt-type"))
                                    }

                                    tr(
                                            td(
                                                    a(name).withHref(linker.siteLinkBetween(element.getParent()!!, element))
                                            ).withClass("element-name-cell"),
                                            td().with(excerptContents).withClass("element-excerpt-cell")
                                    )
                                }
                        )
                ).withClass("elements-table")
        ).withClass("element-type-group")
    }

    private fun getHumanReadableTypeName(element: TopLevelElement) = when (element) {
        is EnumElement -> "enum"
        is Function -> "function"
        is MacroConstant -> "macro constant"
        is MacroFunction -> "macro function"
        is TypeSynonym -> "type synonym"
        is Struct -> "struct"
        is Union -> "union"
        is Variable -> "variable"
        else -> "unknown"
    }

    private fun nameForElement(element: TopLevelElement) = when (element) {
        is EnumElement -> element.name ?: "unnamed enum"
        is Function -> element.name
        is MacroConstant -> element.name
        is MacroFunction -> element.name
        is TypeSynonym -> element.newName
        is Struct -> element.name
        is Union -> element.name
        is Variable -> element.name
        else -> "unknown"
    }

    private fun excerptForElement(element: TopLevelElement) = when (element) {
        is EnumElement -> element.documentation?.excerpt
        is Function -> element.documentation?.excerpt
        is MacroConstant -> element.documentation?.excerpt
        is MacroFunction -> element.documentation?.excerpt
        is TypeSynonym -> element.documentation?.excerpt
        is Struct -> element.documentation?.excerpt
        is Union -> element.documentation?.excerpt
        is Variable -> element.documentation?.excerpt
        else -> null
    }

    private data class HeaderGroup(val name: String, val description: String?)
}