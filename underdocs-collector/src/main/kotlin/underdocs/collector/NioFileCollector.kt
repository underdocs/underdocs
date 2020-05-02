package underdocs.collector

import underdocs.configuration.CollectorConfiguration
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.PathMatcher
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
                .map { it.toString() }
    }

    private fun filesInDirectory(directory: String): Stream<Path> =
            Files
                .find(Paths.get(directory),
                    Integer.MAX_VALUE,
                    BiPredicate { _, attrs -> attrs.isRegularFile })

    private fun compileExpressionStrings(expressions: List<String>): List<PathMatcher> =
            expressions.stream()
                .map { FileSystems.getDefault().getPathMatcher("glob:" + it) }
                .toList()

    private fun matchesExpressions(path: Path, inclusions: List<PathMatcher>, exclusions: List<PathMatcher>): Boolean =
        inclusions.any { it.matches(path) } && exclusions.all { !it.matches(path) }
}