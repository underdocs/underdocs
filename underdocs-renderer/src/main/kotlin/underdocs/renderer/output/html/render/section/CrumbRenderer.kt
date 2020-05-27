package underdocs.renderer.output.html.render.section

import j2html.TagCreator.a
import j2html.TagCreator.div
import j2html.TagCreator.span
import j2html.TagCreator.text
import j2html.tags.DomContent
import j2html.tags.Tag
import underdocs.renderer.output.html.link.Linker
import underdocs.renderer.output.html.render.misc.NameVisitor
import underdocs.renderer.representation.Header
import underdocs.renderer.representation.Module
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.Visitable

class CrumbRenderer(private val linker: Linker) {
    fun render(element: Visitable): Tag<*> {
        val crumbElements = collectElementsFor(element)

        return renderFromCrumbSegments(element, crumbElements)
    }

    private fun renderFromCrumbSegments(from: Visitable, segments: MutableList<Visitable>): Tag<*> {
        val crumbContents = ArrayList<DomContent>()
        val nameVisitor = NameVisitor()

        for (element in segments.asReversed()) {
            element.accept(nameVisitor)
            val title = nameVisitor.name

            val segmentContent = if (from == element) {
                text(title)
            } else {
                a(title).withHref(linker.siteLinkBetween(from, element))
            }

            crumbContents.add(span(segmentContent).withClass("breadcrumb-segment"))

            if (from != element) {
                crumbContents.add(span("/").withClass("breadcrumb-separator"))
            }
        }

        return div().with(crumbContents).withClass("breadcrumb")
    }

    private fun collectElementsFor(obj: Visitable): MutableList<Visitable> = when (obj) {
        is Module -> elementsFor(obj, ArrayList())
        is Header -> elementsFor(obj, ArrayList())
        is TopLevelElement -> elementsFor(obj, ArrayList())
        else -> ArrayList()
    }

    private fun elementsFor(module: Module, crumb: MutableList<Visitable>): MutableList<Visitable> {
        var mod: Module? = module

        while (mod != null) {
            crumb.add(mod)

            mod = mod.parent
        }

        return crumb
    }

    private fun elementsFor(header: Header, crumb: MutableList<Visitable>): MutableList<Visitable> {
        crumb.add(header)

        return if (header.parent == null) {
            crumb
        } else {
            elementsFor(header.parent!!, crumb)
        }
    }

    private fun elementsFor(topLevelElement: TopLevelElement, crumb: MutableList<Visitable>): MutableList<Visitable> {
        crumb.add(topLevelElement)

        return if (topLevelElement.getParent() == null) {
            crumb
        } else {
            elementsFor(topLevelElement.getParent()!!, crumb)
        }
    }
}