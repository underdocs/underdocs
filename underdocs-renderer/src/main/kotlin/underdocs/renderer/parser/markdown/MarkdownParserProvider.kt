package underdocs.renderer.parser.markdown

import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet

object MarkdownParserProvider {
    fun provide(): Parser {
        val options = MutableDataSet()

        options.set(Parser.EXTENSIONS, listOf(TablesExtension.create(), YamlFrontMatterExtension.create()))

        return Parser.builder(options).build()
    }
}
