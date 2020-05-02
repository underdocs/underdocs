package underdocs.collector

import underdocs.configuration.CollectorConfiguration
import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.BiPredicate
import java.util.stream.Stream
import kotlin.streams.toList

class NioFileCollector: FileCollector {
    override fun collectFilesFromDirectory(configuration: CollectorConfiguration): Stream<String> {
        val inclusionExpressions = compileExpressionStrings(configuration.includingPatterns)
        val exclusionExpressions = compileExpressionStrings(configuration.excludingPatterns)

       return filesInDirectory(configuration.includePath)
                .filter { matchesExpressions(it, inclusionExpressions, exclusionExpressions) }
    }

    private fun filesInDirectory(directory: String): Stream<String> =
            Files
                .find(Paths.get(directory),
                    Integer.MAX_VALUE,
                    BiPredicate { _, attrs -> attrs.isRegularFile })
                .map { it.toString() }

    private fun compileExpressionStrings(expressions: List<String>): List<Regex> =
            expressions.stream()
                .map { Regex(it) }
                .toList()

    private fun matchesExpressions(path: String, inclusions: List<Regex>, exclusions: List<Regex>): Boolean =
        inclusions.any { it.matches(path) } && exclusions.all { !it.matches(path) }
}