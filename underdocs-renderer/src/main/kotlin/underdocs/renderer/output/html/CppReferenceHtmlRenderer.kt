package underdocs.renderer.output.html

import underdocs.configuration.RendererConfiguration
import underdocs.renderer.output.html.link.DefaultLinker
import underdocs.renderer.output.html.render.page.PageRenderer
import underdocs.renderer.output.html.render.section.SectionRenderer
import underdocs.renderer.representation.Header
import underdocs.renderer.representation.Module
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.Visitable
import underdocs.renderer.writer.DefaultResourceWriter
import underdocs.renderer.writer.DirectoryCreatingOutputWriter
import java.io.File
import java.nio.file.Paths

class CppReferenceHtmlRenderer(
    private val configuration: RendererConfiguration,
    private val topLevelModule: Module
) {
    private val INTERNAL_RESOURCES = listOf<String>(
        "code.css",
        "file-link-icon.svg",
        "main.css"
    )

    private val STATIC_RESOURCE_DIRECTORY_NAME = "_static"

    private val linker = DefaultLinker(
        configuration,
        Paths.get(topLevelModule.path).toString()
    )
    private val sectionRenderer = SectionRenderer(linker)
    private val pageRenderer = PageRenderer(linker, sectionRenderer)
    private val outputWriter = DirectoryCreatingOutputWriter()
    private val resourceWriter = DefaultResourceWriter()

    fun render() {
        copyInternalResources()

        copyExternalResources()

        emit(topLevelModule)
    }

    private fun copyInternalResources() {
        INTERNAL_RESOURCES.forEach { resource ->
            val systemDependentResource = resource.replace("/", File.separator)
            val outputPath = Paths.get(configuration.outputDirectory, STATIC_RESOURCE_DIRECTORY_NAME, systemDependentResource).toString()

            resourceWriter.writeInternalResource("/$resource", outputPath)
        }
    }

    private fun copyExternalResources() {
        configuration.staticResourceDirectory?.let {
            resourceWriter.writeExternalResources(it, Paths.get(configuration.outputDirectory, STATIC_RESOURCE_DIRECTORY_NAME).toString())
        }
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

    private fun output(element: Visitable, document: String) {
        val outputPath = linker.localFileOutputPathToVisitable(element)

        outputWriter.write(document, outputPath)
    }
}
