package underdocs.renderer.output.html.render.page

import j2html.TagCreator.body
import j2html.TagCreator.div
import j2html.TagCreator.document
import j2html.TagCreator.head
import j2html.TagCreator.html
import j2html.TagCreator.link
import j2html.TagCreator.main
import j2html.TagCreator.script
import j2html.TagCreator.meta
import j2html.tags.Tag
import j2html.tags.UnescapedText
import underdocs.renderer.output.html.link.Linker
import underdocs.renderer.output.html.render.section.CrumbRenderer
import underdocs.renderer.output.html.render.section.SectionRenderer
import underdocs.renderer.representation.Header
import underdocs.renderer.representation.Module
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.Visitable

class PageRenderer(private val linker: Linker,
                   private val sectionRenderer: SectionRenderer) {
    companion object {
        private const val KATEX_VERSION = "0.11.1"
        private const val PRISM_VERSION = "1.20.0"
    }

    private val crumbRenderer = CrumbRenderer(linker)
    private val moduleRenderer = ModuleRenderer(sectionRenderer)
    private val headerRenderer = HeaderRenderer(linker, sectionRenderer)
    private val topLevelElementRenderer = TopLevelElementRenderer(linker, sectionRenderer)

    fun render(element: TopLevelElement) = page(
            element, topLevelElementRenderer.render(element)
    )

    fun render(header: Header) = page(
            header, headerRenderer.render(header)
    )

    fun render(module: Module) = page(
            module, moduleRenderer.render(module)
    )

    private fun page(visitable: Visitable, contents: Tag<*>) = document(html(
            head(
                    meta()
                        .withName("viewport")
                        .withContent("width=device-width, initial-scale=1"),
                    katexCss(),
                    link()
                            .withRel("stylesheet")
                            .withHref(linker.siteLinkBetween(visitable, "_static/main.css")),
                    link()
                            .withRel("stylesheet")
                            .withHref(linker.siteLinkBetween(visitable, "_static/code.css"))
            ),
            body(
                    main(
                            crumbRenderer.render(visitable),
                            contents
                    ),
                    div(
                            script()
                                    .withSrc("https://cdnjs.cloudflare.com/ajax/libs/prism/$PRISM_VERSION/components/prism-core.min.js"),
                            script()
                                    .withSrc("https://cdnjs.cloudflare.com/ajax/libs/prism/$PRISM_VERSION/components/prism-c.min.js"),
                            script()
                                    .withSrc("https://cdnjs.cloudflare.com/ajax/libs/prism/$PRISM_VERSION/plugins/autoloader/prism-autoloader.min.js"),
                            script()
                                    .withSrc("https://cdnjs.cloudflare.com/ajax/libs/prism/$PRISM_VERSION/plugins/line-numbers/prism-line-numbers.min.js"),
                            script()
                                    .withSrc("https://cdn.jsdelivr.net/npm/katex@$KATEX_VERSION/dist/katex.min.js")
                                    .attr("crossorigin", "anonymous"),
                            script()
                                    .with(UnescapedText("""
                                        (function () {
                                              document.addEventListener('DOMContentLoaded', function () {
                                                const mathElements = Array.from(document.getElementsByClassName('katex-inline'))
                                                    .concat(Array.from(document.getElementsByClassName('katex-block')));
                                                
                                                console.log(mathElements.length)

                                                mathElements.forEach(element => {
                                                    katex.render(element.textContent, element, { 
                                                        throwOnError: false,
                                                        displayMode: element.classList.contains('katex-block'), 
                                                    });
                                                });
                                               })
                                        })();
                                    """.trimIndent()))
                    )
            )
    ))

    private fun katexCss() = link()
            .withRel("stylesheet")
            .withHref("https://cdn.jsdelivr.net/npm/katex@$KATEX_VERSION/dist/katex.min.css")
            .attr("crossorigin", "anonymous")
}
