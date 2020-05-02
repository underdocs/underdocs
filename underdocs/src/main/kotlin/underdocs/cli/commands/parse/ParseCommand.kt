package underdocs.cli.commands.parse

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.path
import underdocs.cli.common.configurationFromPath
import underdocs.cli.filecollector.NioFileCollector
import underdocs.parser.HeaderParser
import underdocs.representation.Codebase
import underdocs.representation.serialization.RepresentationSerializer
import underdocs.parser.configuration.EclipseParserConfiguration
import java.nio.file.Paths

class ParseCommand: CliktCommand(
        name = "parse",
        help = "Parse"
) {
    val configurationPath by option("-c", "--configurationPath").path(
            exists = true,
            fileOkay = true,
            folderOkay = false,
            readable = true
    ).required()

    override fun run() {
        val configuration = configurationFromPath(configurationPath)

        val headerParser = HeaderParser.create(configuration.parser)

        val resolvedIncludePath = configurationPath.parent.toAbsolutePath().resolve(configuration.parser.includePath)

        val headerStream = NioFileCollector().collectFilesFromDirectory(
                resolvedIncludePath.toString(),
                configuration.parser.includingPatterns,
                configuration.parser.excludingPatterns
        )

        val headers = headerStream
                .map { headerParser.parse(it) }
                .toList()

        val codebase = Codebase(
                configuration.parser.includePath,
                headers
        )

        val codebaseSerializer = RepresentationSerializer.create()

        codebaseSerializer.writeInto(Paths.get(configuration.parser.outputFile), codebase)

        // TODO: Remove!
        val rendererFactory = CppReferenceRendererFactory()

        val rendererConfiguration = _root_ide_package_.underdocs.renderer.CppReferenceRendererConfiguration()

        val renderer = rendererFactory.createInstance(rendererConfiguration)

        renderer.renderCodebase(codebase)
    }
}
