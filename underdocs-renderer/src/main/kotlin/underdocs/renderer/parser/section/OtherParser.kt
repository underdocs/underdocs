package underdocs.renderer.parser.section

import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node

class OtherParser(private val knownSectionNames: Set<String>) : underdocs.renderer.parser.section.SectionParser<Map<String, String>> {
    override fun tryParse(document: Document): Map<String, String>? {
        val result = mutableMapOf<String, String>()

        var currentSectionHeading = underdocs.renderer.parser.section.nextNodeWhere(document.firstChild) {
            isOtherSectionHeading(it)
        }

        while (currentSectionHeading != null) {
            val nextSectionHeading = underdocs.renderer.parser.section.nextNodeWhere(currentSectionHeading) {
                underdocs.renderer.parser.section.isSectionHeading(it)
            }
            val nextOtherSectionHeading = underdocs.renderer.parser.section.nextNodeWhere(currentSectionHeading) {
                isOtherSectionHeading(it)
            }

            val groupTitle = (currentSectionHeading as Heading).text.toString()
            val groupText = underdocs.renderer.parser.section.extractTextBetweenNodes(
                currentSectionHeading.next,
                nextSectionHeading
                    ?: document.lastChild
            )

            result[groupTitle] = groupText

            currentSectionHeading = nextOtherSectionHeading
        }

        return result
    }

    private fun isOtherSectionHeading(node: Node) =
        underdocs.renderer.parser.section.isSectionHeading(node) && (node as Heading).text.toString() !in knownSectionNames
}
