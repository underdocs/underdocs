package underdocs.renderer.parser.documentation

import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterBlock
import com.vladsch.flexmark.util.ast.Document
import underdocs.renderer.parser.section.extractTextBetweenNodes
import underdocs.renderer.representation.documentation.MemberDocumentation

class MemberDocumentationParser: underdocs.renderer.parser.documentation.DocumentationParser<MemberDocumentation> {
    private val attributesParser = underdocs.renderer.parser.section.AttributesParser()

    override fun parse(document: Document): MemberDocumentation {
        return MemberDocumentation(
                attributesParser.tryParse(document) ?: emptyMap(),
                extractDescription(document)
        )
    }

    private fun extractDescription(document: Document): String? {
        val startNode = (if (document.firstChild is YamlFrontMatterBlock) {
            document.firstChild.next
        } else {
            document.firstChild
        }) ?: return null

        val endNode = document.lastChild

        return extractTextBetweenNodes(startNode, endNode)
    }
}
