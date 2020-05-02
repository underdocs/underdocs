package underdocs.renderer.output.html

import j2html.tags.Tag
import underdocs.configuration.RendererConfiguration
import underdocs.renderer.output.html.render.section.SectionRenderer
import underdocs.renderer.representation.Header
import underdocs.renderer.representation.Module
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.Visitable
import java.nio.file.Paths

class CppReferenceHtmlRenderer(
        private val configuration: RendererConfiguration,
        private val topLevelModule: Module
) {
    private val sectionRenderer = SectionRenderer()
    private val pageRenderer = underdocs.renderer.output.html.render.page.PageRenderer(sectionRenderer)
    private val outputWriter = underdocs.renderer.writer.DirectoryCreatingOutputWriter()
    private val linker = underdocs.renderer.output.html.link.DefaultLinker(
            Paths.get(topLevelModule.path).toString(),
            configuration.outputDirectory
    )

    fun render() {
        emit(topLevelModule)
    }

    private fun emit(module: Module) {
        val document = pageRenderer.render(module)

        output(module, document)

        /*module.headers.forEach {
            emit(it)
        }*/

        module.children.values.forEach {
            emit(it)
        }
    }

    private fun emit(header: Header) {
        val document = pageRenderer.render(header)

        output(header, document)

        /*header.elements
                .values
                .stream()
                .flatMap { it.stream() }
                .forEach {
                    emit(it)
                }*/
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