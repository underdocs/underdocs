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

        val successItemList = mutableListOf<ReturnValueItem>()

        val startNode = document.children.first { isSectionHeadingWithTitle(it, "Return Value") }
        val successHeading = nextNodeInSectionWhere(startNode) { isCustomHeading(it, "Success") } as Heading

        val successList = successHeading.next

        if (successList == null || successList !is BulletList) {
            return ReturnValue(mutableListOf(), mutableListOf())
        }

        for (success in successList.children) {
            createReturnValueItemList(success, successItemList)
        }

        val errorItemList = mutableListOf<ReturnValueItem>()

        val errorHeading = nextNodeInSectionWhere(startNode) { isCustomHeading(it, "Error") } as Heading
        val errorList = errorHeading.next

        if (errorList == null || errorList !is BulletList) {
            return ReturnValue(successItemList, mutableListOf())
        }

        for (error in errorList.children) {
            createReturnValueItemList(error, errorItemList)
        }

        return ReturnValue(successItemList, errorItemList)

    }

    private fun createReturnValueItemList(element: Node?, itemList: MutableList<ReturnValueItem>) {

        if (element !is BulletListItem) {
            return
        }

        val value = element.firstChild.chars.toString().trim()

        val valueDescriptionList = element.firstChild.next

        if (valueDescriptionList !is BulletList) {
            return
        }

        val valueDescriptionItem = valueDescriptionList.firstChild as BulletListItem

        val valueDescription = extractTextBetweenNodes(
            valueDescriptionItem.firstChild,
            valueDescriptionItem.lastChild
        )

        itemList.add(ReturnValueItem(value, valueDescription))
    }

    private fun isCustomHeading(node: Node, title: String) =
        node is Heading && node.level == 3 && node.text.toString() == title

}