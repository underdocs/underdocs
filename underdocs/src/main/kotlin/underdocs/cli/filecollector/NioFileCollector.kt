package underdocs.cli.filecollector

import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.BiPredicate
import java.util.stream.Stream
import kotlin.streams.toList

class NioFileCollector: FileCollector {
    override fun collectFilesFromDirectory(directory: String, inclusions: List<String>, exclusions: List<String>): Stream<String> {
        val inclusionExpressions = compileExpressionStrings(inclusions)
        val exclusionExpressions = compileExpressionStrings(exclusions)

       return filesInDirectory(directory)
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