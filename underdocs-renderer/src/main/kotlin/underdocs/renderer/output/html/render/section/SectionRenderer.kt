package underdocs.renderer.output.html.render.section

import j2html.TagCreator.div
import j2html.TagCreator.each
import j2html.TagCreator.h2
import j2html.TagCreator.li
import j2html.TagCreator.ul
import j2html.tags.Tag
import underdocs.renderer.representation.Module
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.documentation.Example

class SectionRenderer {
    private val titleRenderer = underdocs.renderer.output.html.render.section.TitleRenderer()

    fun renderTitle(name: String, attributes: Map<String, String>) =
            titleRenderer.render(name, attributes)

    fun renderRepresentation(element: TopLevelElement): Tag<*> {
        TODO()
    }

    fun renderExcerpt(excerpt: String): Tag<*> {
        TODO()
    }

    fun renderDescription(description: String): Tag<*> {
        TODO()
    }

    fun renderParameters(parameters: Map<String, String>): Tag<*> {
        TODO()
    }

    fun renderExamples(examples: List<Example>): Tag<*> {
        TODO()
    }

    fun renderSeeAlso(elements: List<String>): Tag<*> {
        TODO()
    }

    fun renderReturnValue(returnValue: String): Tag<*> {
        TODO()
    }

    fun renderErrorHandling(errorHandling: String): Tag<*> {
        TODO()
    }

    fun renderNotes(notes: String): Tag<*> {
        TODO()
    }

    fun renderOtherSections(otherSections: Map<String, String>): Tag<*> {
        TODO()
    }

    fun renderSubmodules(module: Module): Tag<*> {
        return div(
                h2("Submodules"),
                div(
                    ul(
                        each(module.children.values) {
                            return@each li(it.documentation?.title)
                        }
                    )
                )
        )
    }

    fun renderSubheaders(module: Module): Tag<*> {
        return div(
                h2("Headers"),
                div(
                        ul(
                                each(module.headers) {
                                    return@each li(it.filename)
                                }
                        )
                )
        )
    }
}