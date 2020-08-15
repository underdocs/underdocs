package underdocs.renderer.parser.section

import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.BulletListItem
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import underdocs.renderer.representation.documentation.ReturnValue
import underdocs.renderer.representation.documentation.ReturnValueItem

class ReturnValueParser : AttemptingSectionParser<ReturnValue>() {
    override fun canParse(document: Document) =
        document.children.any { isSectionHeadingWithTitle(it, "Return Value") }

    override fun parse(document: Document): ReturnValue {
        val startNode = document.children.first { isSectionHeadingWithTitle(it, "Return Value") }

        val successItemList = parseItemList(startNode, "Success")
        val errorItemList = parseItemList(startNode, "Error")

        return ReturnValue(successItemList, errorItemList)
    }

    private fun parseItemList(startNode: Node, headingTitle: String): List<ReturnValueItem> {
        val heading = nextNodeInSectionWhere(startNode) {
            isHeading(
                node = it,
                level = 3,
                title = headingTitle
            )
        } as Heading?

        val elementList = heading?.next

        if (elementList == null || elementList !is BulletList) {
            return emptyList()
        }

        return elementList.children
            .mapNotNull { createReturnValueItem(it) }
            .toList()
    }

    private fun createReturnValueItem(element: Node?): ReturnValueItem? {
        if (element !is BulletListItem) {
            return null
        }

        val value = element.firstChild.chars.toString().trim()

        val valueDescriptionList = element.firstChild.next
        if (valueDescriptionList !is BulletList) {
            return null
        }
        val valueDescriptionItem = valueDescriptionList.firstChild as BulletListItem
        val valueDescription = extractTextBetweenNodes(valueDescriptionItem.firstChild, valueDescriptionItem.lastChild)

        return ReturnValueItem(value, valueDescription)
    }
}
