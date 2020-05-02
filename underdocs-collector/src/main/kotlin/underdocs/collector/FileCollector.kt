package underdocs.collector

import underdocs.configuration.CollectorConfiguration
import java.util.stream.Stream

interface FileCollector {
    companion object {
        fun create(): FileCollector = NioFileCollector()
    }

    fun collectFilesFromDirectory(configuration: CollectorConfiguration): Stream<String>
}   