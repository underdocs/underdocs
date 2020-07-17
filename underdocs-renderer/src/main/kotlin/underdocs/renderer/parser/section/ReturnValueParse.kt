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

        val result = ReturnValue(successItemList, mutableListOf())

        val startNode = document.children.first { isSectionHeadingWithTitle(it, "Return Value") }
        val successHeading = nextNodeInSectionWhere(startNode) { isSuccessHeading(it) } as Heading

        val successList = successHeading.next

        if (successList == null || successList !is BulletList) {
            return ReturnValue(mutableListOf(), mutableListOf())
        }

        for (success in successList.children) {
            if (success !is BulletListItem) {
                continue
            }

            val value = success.firstChild.chars.toString().trim()

            val valueDescriptionList = success.firstChild.next

            if (valueDescriptionList !is BulletList) {
                continue
            }

            val valueDescriptionItem = valueDescriptionList.firstChild as BulletListItem

            val valueDescription = extractTextBetweenNodes(valueDescriptionItem.firstChild,
                valueDescriptionItem.lastChild)

            successItemList.add(ReturnValueItem(value, valueDescription))

        }

        return result
    }

    private fun isSuccessHeading(node: Node) =
        node is Heading && node.level == 3 && node.text.toString() == "Success"

    private fun isErrorHeading(node: Node) =
        node is Heading && node.level == 3 && node.text.toString() == "Error"
}