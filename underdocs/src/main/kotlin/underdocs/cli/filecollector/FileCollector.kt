package underdocs.cli.filecollector

import java.util.stream.Stream

interface FileCollector {
    fun collectFilesFromDirectory(directory: String, inclusions: List<String>, exclusions: List<String>): Stream<String>
}