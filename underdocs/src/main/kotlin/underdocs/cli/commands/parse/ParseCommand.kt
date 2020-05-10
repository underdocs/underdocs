package underdocs.cli.commands.parse

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.path
import org.slf4j.LoggerFactory
import underdocs.collector.FileCollector
import underdocs.configuration.CollectorConfiguration
import underdocs.configuration.ParserConfiguration
import underdocs.configuration.domain.CommentStyle.BLOCK_SLASH_DOUBLE_STAR
import underdocs.configuration.domain.CommentStyle.BLOCK_SLASH_STAR
import underdocs.configuration.domain.CommentStyle.SINGLE_LINE_DOUBLE_SLASH
import underdocs.configuration.domain.CommentStyle.SINGLE_LINE_TRIPLE_SLASH
import underdocs.parser.HeaderParser
import underdocs.representation.Codebase
import underdocs.representation.serialization.RepresentationSerializer
import java.nio.file.Paths
import kotlin.streams.toList

class ParseCommand : CliktCommand(
        name = "parse",
        help = "Parse"
) {
    private val log = LoggerFactory.getLogger(ParseCommand::class.java)

    val includePath by option("-I", "--includePath")
            .path(
                    exists = true,
                    folderOkay = true,
                    readable = true
            )
            .required()

    val outputPath by option("-o", "--output")
            .path()
            .required()

    val includePatterns by option("--includePattern")
            .multiple(listOf("**/*.h"))

    val excludePatterns by option("--excludePatterns")
            .multiple(emptyList())

    val documentationCommentStyles by option("--documentationCommentStyle")
            .choice(mapOf(
                    "BLOCK_SLASH_STAR" to BLOCK_SLASH_STAR,
                    "BLOCK_SLASH_DOUBLE_STAR" to BLOCK_SLASH_DOUBLE_STAR,
                    "SINGLE_LINE_DOUBLE_SLASH" to SINGLE_LINE_DOUBLE_SLASH,
                    "SINGLE_LINE_TRIPLE_SLASH" to SINGLE_LINE_TRIPLE_SLASH
            ))
            .multiple(listOf(BLOCK_SLASH_DOUBLE_STAR, SINGLE_LINE_DOUBLE_SLASH))

    override fun run() {
        log.info(includePath.toAbsolutePath().toString())
        log.info(outputPath.toAbsolutePath().toString())

        val collectorConfiguration = CollectorConfiguration(
                includePath = includePath.toAbsolutePath().toString(),
                includingPatterns = includePatterns,
                excludingPatterns = excludePatterns
        )

        val headerStream = FileCollector.create()
                .collectFilesFromDirectory(collectorConfiguration)

        val parserConfiguration = ParserConfiguration(
                documentationCommentStyles = HashSet(documentationCommentStyles)
        )

        val headerParser = HeaderParser.create(parserConfiguration)

        val headers = headerStream
                .map { headerParser.parse(it) }
                .toList()

        log.info("Headers: {}", headers.size)

        val codebase = Codebase(
                collectorConfiguration.includePath,
                headers
        )

        val codebaseSerializer = RepresentationSerializer.create()

        codebaseSerializer.writeInto(Paths.get(outputPath.toString()), codebase)
    }
}
