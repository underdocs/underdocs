package underdocs.renderer.parser.section

import com.vladsch.flexmark.util.ast.Document

class ExcerptParser : underdocs.renderer.parser.section.AttemptingSectionParser<String>() {
    override fun canParse(document: Document) =
        hasExcerptSection(document) || hasDescriptionSection(document)

    override fun parse(document: Document) =
        if (hasExcerptSection(document)) {
            parseExcerpt(document)
        } else {
            parseDescription(document)
        }

    private fun hasExcerptSection(document: Document) =
        document.children.any { underdocs.renderer.parser.section.isSectionHeadingWithTitle(it, "Excerpt") }

    private fun hasDescriptionSection(document: Document) =
        document.children.any { underdocs.renderer.parser.section.isSectionHeadingWithTitle(it, "Description") }

    private fun parseExcerpt(document: Document) =
        textInSection(document, "Excerpt")

    private fun parseDescription(document: Document): String {
        val text = textInSection(document, "Description")

        val firstSentence = text.substringBefore(". ")

        return "$firstSentence"
    }

    private fun textInSection(document: Document, sectionTitle: String): String {
        val startNode = document.children.first {
            underdocs.renderer.parser.section.isSectionHeadingWithTitle(it, sectionTitle)
        }

        val endNode = underdocs.renderer.parser.section.lastNodeBeforeOrLast(startNode) {
            underdocs.renderer.parser.section.isSectionHeading(it)
        }

        return underdocs.renderer.parser.section.extractTextBetweenNodes(startNode.next, endNode)
    }
}
