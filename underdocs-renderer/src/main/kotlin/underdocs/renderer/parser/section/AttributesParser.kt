package underdocs.renderer.parser.section

import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterBlock
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterNode
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterValue
import com.vladsch.flexmark.util.ast.Document

class AttributesParser : AttemptingSectionParser<Map<String, String>>() {
    override fun canParse(document: Document) =
        document.firstChild is YamlFrontMatterBlock

    override fun parse(document: Document): Map<String, String> {
        val result = mutableMapOf<String, String>()

        for (child in document.firstChild.children) {
            if (child !is YamlFrontMatterNode) {
                continue
            }

            val value = child.children.firstOrNull { it is YamlFrontMatterValue } as YamlFrontMatterValue?
                ?: continue

            result[child.key] = value.chars.toString()
        }

        return result
    }
}
