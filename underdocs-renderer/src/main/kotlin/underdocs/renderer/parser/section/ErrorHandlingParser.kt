package underdocs.renderer.parser.section

import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.BulletListItem
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import underdocs.renderer.representation.documentation.ErrorHandling
import underdocs.renderer.representation.documentation.ErrorHandlingItem

class ErrorHandlingParser : AttemptingSectionParser<ErrorHandling>() {
    override fun canParse(document: Document) =
        document.children.any { isSectionHeadingWithTitle(it, "Error Handling") }

    override fun parse(document: Document): ErrorHandling {

        val startNode = document.children.first { isSectionHeadingWithTitle(it, "Error Handling") }

        val errorHandlingItemList = createErrorHandlingItemList(startNode)

        return ErrorHandling(errorHandlingItemList)
    }

    private fun createErrorHandlingItemList(startNode: Node): List<ErrorHandlingItem> {

        val errorHandlingElementList = startNode.next

        if (errorHandlingElementList == null || errorHandlingElementList !is BulletList) {
            return emptyList()
        }

        val errorHandlingItemList = mutableListOf<ErrorHandlingItem>()

        for (errorHandlingElement in errorHandlingElementList.children) {
            if (errorHandlingElement !is BulletListItem) {
                continue
            }

            val actionState = errorHandlingElement.firstChild.chars.toString().trim()

            val conditionElementList = errorHandlingElement.firstChild.next

            if (conditionElementList !is BulletList) {
                continue
            }

            val conditionElement = conditionElementList.firstChild as BulletListItem

            val condition = extractTextBetweenNodes(conditionElement.firstChild, conditionElement.lastChild)

            errorHandlingItemList.add(ErrorHandlingItem(actionState, condition))
        }

        return errorHandlingItemList
    }
}