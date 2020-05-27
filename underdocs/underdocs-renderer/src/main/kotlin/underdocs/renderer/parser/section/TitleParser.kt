package underdocs.renderer.parser.section

import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node

class TitleParser : underdocs.renderer.parser.section.AttemptingSectionParser<String>() {
    override fun canParse(document: Document) =
            isTitleHeading(document.firstChild) || isTitleHeading(document.firstChild?.next)

    override fun parse(document: Document) =
            getTitleNode(document).text.toString()

    private fun isTitleHeading(node: Node?) =
            node is Heading && node.level == 1

    private fun getTitleNode(document: Document): Heading =
            if (isTitleHeading(document.firstChild)) {
                document.firstChild as Heading
            } else {
                document.firstChild.next as Heading
            }
}
