package underdocs.renderer.output.html.render.section

import j2html.tags.Tag
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.documentation.Example

class SectionRenderer {
    private val titleRenderer = _root_ide_package_.underdocs.renderer.output.html.render.section.TitleRenderer()

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
}