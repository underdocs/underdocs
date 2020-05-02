package underdocs.renderer.parser.module

import underdocs.renderer.parser.tree.ModuleNode
import underdocs.renderer.representation.Module

interface ModuleParser {
    companion object {
        fun create(): underdocs.renderer.parser.module.ModuleParser =
                underdocs.renderer.parser.module.DefaultModuleParser(underdocs.renderer.parser.markdown.MarkdownParserProvider.provide())
    }

    fun parse(moduleNode: ModuleNode): Module
}
