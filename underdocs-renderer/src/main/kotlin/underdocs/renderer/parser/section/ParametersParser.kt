package underdocs.renderer.parser.section

import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.BulletListItem
import com.vladsch.flexmark.util.ast.Document

class ParametersParser: _root_ide_package_.underdocs.renderer.parser.section.AttemptingSectionParser<Map<String, String>>() {
    override fun canParse(document: Document) =
            document.children.any { _root_ide_package_.underdocs.renderer.parser.section.isSectionHeadingWithTitle(it, "Parameters") }

    override fun parse(document: Document): Map<String, String> {
        val result = mutableMapOf<String, String>()

        val startNode = document.children.first { _root_ide_package_.underdocs.renderer.parser.section.isSectionHeadingWithTitle(it, "Parameters") }

        val sectionEndNode = _root_ide_package_.underdocs.renderer.parser.section.sectionEndNodeFrom(startNode)

        val parameterList = startNode.next

        if (parameterList == null || parameterList !is BulletList) {
            return result
        }

        for (parameter in parameterList.children) {
            if (parameter !is BulletListItem) {
                continue
            }

            val parameterName = parameter.firstChild.chars.toString()

            val parameterDescriptionList = parameter.firstChild.next

            if (parameterDescriptionList !is BulletList) {
                continue
            }

            val parameterDescriptionItem = parameterDescriptionList.firstChild as BulletListItem

            val parameterDescription = _root_ide_package_.underdocs.renderer.parser.section.extractTextBetweenNodes(parameterDescriptionItem.firstChild, parameterDescriptionItem.lastChild)

            result[parameterName] = parameterDescription
        }

        return result
    }
}