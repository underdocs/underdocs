package underdocs.renderer.parser.section

import com.vladsch.flexmark.util.ast.Document

class TextSectionParser(private val sectionTitle: String): underdocs.renderer.parser.section.AttemptingSectionParser<String>() {
    override fun canParse(document: Document) =
            document.children.any { isSectionHeadingWithTitle(it, sectionTitle) }

    override fun parse(document: Document): String {
        val startNode = document.children.first {
            isSectionHeadingWithTitle(it, sectionTitle)
        }

        val endNode = lastNodeBeforeOrLast(startNode) {
            isSectionHeading(it)
        }

        return extractTextBetweenNodes(startNode.next, endNode)
    }
}
