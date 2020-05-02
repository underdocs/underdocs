package underdocs.cli.commands.render

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.path
import underdocs.configuration.RendererConfiguration
import underdocs.renderer.CodebaseRenderer
import underdocs.representation.serialization.RepresentationSerializer

class RenderCommand: CliktCommand(
        name = "render",
        help = "Render"
) {
    val inputCodebase by option("-i", "--inputCodebase")
            .path(
                    exists = true,
                    fileOkay = true,
                    folderOkay = false,
                    readable = true
            )
            .required()

    val outputDirectory by option("-o", "--outputDirectory")
            .path(
                    exists = false
            )
            .required()

    override fun run() {
        val rendererConfiguration = RendererConfiguration(
                outputDirectory = outputDirectory.toString()
        )

        val codebaseSerializer = RepresentationSerializer.create()

        val codebase = codebaseSerializer.readFrom(inputCodebase)

        val renderer = CodebaseRenderer.create(rendererConfiguration)

        renderer.render(codebase)
    }
}
