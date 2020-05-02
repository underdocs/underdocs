package underdocs.renderer.parser.section

import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node

class GroupsParser: _root_ide_package_.underdocs.renderer.parser.section.AttemptingSectionParser<Map<String, String>>() {
    override fun canParse(document: Document) =
            document.children.any { _root_ide_package_.underdocs.renderer.parser.section.isSectionHeadingWithTitle(it, "Groups") }

    override fun parse(document: Document): Map<String, String> {
        val result = mutableMapOf<String, String>()

        val startNode = document.firstChild

        var currentGroupHeading = _root_ide_package_.underdocs.renderer.parser.section.nextNodeInSectionWhere(startNode) { isGroupHeading(it) }

        val sectionEndNode = _root_ide_package_.underdocs.renderer.parser.section.sectionEndNodeFrom(startNode)

        while (currentGroupHeading != null) {
            val nextGroupHeading = _root_ide_package_.underdocs.renderer.parser.section.nextNodeInSectionWhere(currentGroupHeading) {
                isGroupHeading(it)
            }

            val groupTitle = (currentGroupHeading as Heading).text.toString()
            val groupText = _root_ide_package_.underdocs.renderer.parser.section.extractTextBetweenNodes(currentGroupHeading.next, nextGroupHeading
                    ?: sectionEndNode)

            result[groupTitle] = groupText

            currentGroupHeading = nextGroupHeading
        }

        return result
    }

    private fun isGroupHeading(node: Node) =
            node is Heading && node.level == 3
}
