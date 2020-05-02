package underdocs.renderer.parser

import underdocs.representation.Header
import underdocs.renderer.parser.tree.ModuleNode
import underdocs.renderer.representation.Module
import java.nio.file.Paths

class CodebaseParserImpl: _root_ide_package_.underdocs.renderer.parser.CodebaseParser {
    private val moduleParser = _root_ide_package_.underdocs.renderer.parser.module.ModuleParser.create()

    override fun parseHeaders(headers: List<Header>): Module {
        val minimumSegmentCount = headers.stream()
                .map { it.path }
                .map { Paths.get(it) }
                .mapToInt { it.nameCount }
                .min()
                .orElse(0)

        val rootModuleNode = groupHeadersIntoModuleNodes(headers, minimumSegmentCount - 1)

        return moduleParser.parse(rootModuleNode)
    }

    private fun groupHeadersIntoModuleNodes(headers: List<Header>, segmentIndex: Int): ModuleNode  {
        val groups = HashMap<String, MutableList<Header>>()
        val moduleHeaders = mutableListOf<Header>()
        val modulePath = headers.firstOrNull()
                ?.let { Paths.get(it.path).subpath(0, segmentIndex).toString() }
                ?: ""

        for (header in headers) {
            val path = Paths.get(header.path)

            if (path.nameCount == segmentIndex + 1) {
                moduleHeaders.add(header)
            } else {
                val segment = path.getName(segmentIndex).toString()

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
}
