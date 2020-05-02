package underdocs.renderer.parser.module

import underdocs.renderer.parser.tree.ModuleNode
import underdocs.renderer.representation.Module

interface ModuleParser {
    companion object {
        fun create(): _root_ide_package_.underdocs.renderer.parser.module.ModuleParser =
                _root_ide_package_.underdocs.renderer.parser.module.ModuleParserImpl(_root_ide_package_.underdocs.renderer.parser.markdown.MarkdownParserProvider.provide())
    }

    fun parse(moduleNode: ModuleNode): Module
}
