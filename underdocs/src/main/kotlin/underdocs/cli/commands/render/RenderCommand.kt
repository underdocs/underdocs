package underdocs.cli.commands.render

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.path
import underdocs.cli.commands.render.loader.DefaultRendererLoader
import underdocs.cli.common.configurationParserFromPath
import underdocs.representation.Codebase
import java.io.File

class RenderCommand: CliktCommand(
        name = "render",
        help = "Render"
) {
    val configurationPath by option("-c", "--configurationPath").path(
            exists = true,
            fileOkay = true,
            folderOkay = false,
            readable = true
    ).required()

    override fun run() {
        val parser = configurationParserFromPath(configurationPath)

        val configuration = parser.getConfiguration()

        val rendererLoader = DefaultRendererLoader()

        val rendererFactory = rendererLoader.loadFromJar(configuration.renderer.path)

        val rendererConfigurationClazz = rendererFactory.getConfigurationClass()

        val rendererConfiguration = parser.getRendererOptionsAs(rendererConfigurationClazz)

        val renderer = rendererFactory.createInstance(rendererConfiguration)

        val mapper = ObjectMapper().registerModule(KotlinModule())

        val codebase = mapper.readValue(File(configuration.renderer.parsedCodebaseFile), Codebase::class.java)

        renderer.renderCodebase(codebase)
    }
}
