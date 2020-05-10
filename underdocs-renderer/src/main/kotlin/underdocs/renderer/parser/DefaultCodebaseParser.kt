package underdocs.renderer.parser

import underdocs.renderer.parser.tree.ModuleNode
import underdocs.renderer.representation.Module
import underdocs.representation.Header
import java.io.File

class DefaultCodebaseParser : underdocs.renderer.parser.CodebaseParser {
    private val moduleParser = underdocs.renderer.parser.module.ModuleParser.create()

    override fun parseHeaders(headers: List<Header>): Module {
        val minimumSegmentCount = headers.stream()
                .map { it.path }
                .mapToInt { it.count { it == File.separatorChar } }
                .min()
                .orElse(0)

        val rootModuleNode = groupHeadersIntoModuleNodes(headers, minimumSegmentCount)

        return moduleParser.parse(rootModuleNode)
    }

    private fun groupHeadersIntoModuleNodes(headers: List<Header>, segmentIndex: Int): ModuleNode {
        val groups = HashMap<String, MutableList<Header>>()
        val moduleHeaders = mutableListOf<Header>()
        val modulePath = headers.firstOrNull()
                ?.let { prefixPath(it.path, segmentIndex) }
                ?: ""

        for (header in headers) {
            if (header.path.count { it == File.separatorChar } == segmentIndex) {
                moduleHeaders.add(header)
            } else {
                val segment = segmentAt(header.path, segmentIndex)

                if (segment !in groups) {
                    groups[segment] = mutableListOf();
                }

                groups[segment]?.add(header)
            }
        }

        return ModuleNode(
                modulePath,
                groups.mapValues { groupHeadersIntoModuleNodes(it.value, segmentIndex + 1) },
                moduleHeaders
        )
    }

    private fun prefixPath(path: String, until: Int) = path.split(File.separator)
            .take(until)
            .joinToString(File.separator)

    private fun segmentAt(path: String, index: Int): String = path.split(File.separator)[index]
}
