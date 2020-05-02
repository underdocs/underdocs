package underdocs.renderer.output.html

import j2html.tags.Tag
import underdocs.renderer.representation.Header
import underdocs.renderer.representation.Module
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.Visitable

class CppReferenceHtmlRenderer private constructor(val topLevelModule: Module) {
    companion object {
        fun getInstance(topLevelModule: Module): underdocs.renderer.output.html.CppReferenceHtmlRenderer {
            return underdocs.renderer.output.html.CppReferenceHtmlRenderer(topLevelModule)
        }
    }

    private val pageRenderer = underdocs.renderer.output.html.render.page.PageRenderer()
    private val outputWriter = underdocs.renderer.output.html.writer.DirectoryCreatingOutputWriter()
    private val linker = underdocs.renderer.output.html.link.HtmlLinker()

    fun render() {
        emit(topLevelModule)
    }

    private fun emit(module: Module) {
        val document = pageRenderer.render(module)

        output(module, document)

        module.headers.forEach {
            emit(it)
        }

        module.children.values.forEach {
            emit(it)
        }
    }

    private fun emit(header: Header) {
        val document = pageRenderer.render(header)

        output(header, document)

        header.elements
                .values
                .stream()
                .flatMap { it.stream() }
                .forEach {
                    emit(it)
                }
    }

    private fun emit(element: TopLevelElement) {
        val document = pageRenderer.render(element)

        output(element, document)
    }

    private fun output(element: Visitable, document: Tag<*>) {
        val outputPath = linker.outputPathFor(element);

        outputWriter.write(document.render(), outputPath);
    }
}