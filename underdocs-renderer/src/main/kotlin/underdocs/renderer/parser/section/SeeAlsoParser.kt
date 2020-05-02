package underdocs.renderer.parser.section

import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.BulletListItem
import com.vladsch.flexmark.util.ast.Document

class SeeAlsoParser: _root_ide_package_.underdocs.renderer.parser.section.AttemptingSectionParser<List<String>>() {
    override fun canParse(document: Document) =
            document.children.any { isSectionHeadingWithTitle(it, "See Also") }

    override fun parse(document: Document): List<String> {
        val result = mutableListOf<String>()

        val startNode = document.children.first { isSectionHeadingWithTitle(it, "See Also") }

        val list = startNode.next

        if (list !is BulletList) {
            return result
        }

        for (item in list.children) {
            if (item !is BulletListItem) {
                continue
            }

            result.add(item.contentChars.toString())
        }

        return result
    }
}