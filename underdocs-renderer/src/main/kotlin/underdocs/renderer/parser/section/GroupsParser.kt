package underdocs.renderer.parser.section

import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node

class GroupsParser : underdocs.renderer.parser.section.AttemptingSectionParser<Map<String, String>>() {
    override fun canParse(document: Document) =
            document.children.any { underdocs.renderer.parser.section.isSectionHeadingWithTitle(it, "Groups") }

    override fun parse(document: Document): Map<String, String> {
        val result = mutableMapOf<String, String>()

        val startNode = document.children.first { isSectionHeadingWithTitle(it, "Groups") }

        var currentGroupHeading = nextNodeInSectionWhere(startNode) { isGroupHeading(it) }

        val sectionEndNode = sectionEndNodeFrom(startNode)

        while (currentGroupHeading != null) {
            val nextGroupHeading = underdocs.renderer.parser.section.nextNodeInSectionWhere(currentGroupHeading) {
                isGroupHeading(it)
            }

            val groupTitle = (currentGroupHeading as Heading).text.toString()
            val groupText = extractTextBetweenNodes(currentGroupHeading.next ?: currentGroupHeading.firstChild, nextGroupHeading
                    ?: sectionEndNode)

            result[groupTitle] = groupText

            currentGroupHeading = nextGroupHeading
        }

        return result
    }

    private fun isGroupHeading(node: Node) =
            node is Heading && node.level == 3
}
