package underdocs.renderer.parser.module

import com.vladsch.flexmark.parser.Parser
import underdocs.renderer.parser.tree.ModuleNode
import underdocs.renderer.representation.Module
import underdocs.representation.Header
import kotlin.streams.toList

class DefaultModuleParser(private val mdParser: Parser) : underdocs.renderer.parser.module.ModuleParser {
    private val moduleInfoPrefixes = setOf(
        "module-info.",
        "module_info"
    )

    private val headerParser = underdocs.renderer.parser.header.HeaderParser.create()

    private val moduleDocumentationParser = underdocs.renderer.parser.documentation.ModuleDocumentationParser()

    override fun parse(moduleNode: ModuleNode): Module {
        val moduleDocumentation = moduleNode.headers.firstOrNull {
            isModuleInfoHeader(it)
        }?.let {
            parseModuleInfo(it)
        }

        val headers = moduleNode.headers.stream()
            .filter { !isModuleInfoHeader(it) }
            .map { headerParser.parse(it) }
            .toList()

        val children = moduleNode.children.mapValues { this.parse(it.value) }

        val module = Module(
            moduleNode.path,
            null,
            moduleDocumentation,
            headers,
            children
        )

        headers.forEach {
            it.parent = module
        }

        children.forEach {
            it.value.parent = module
        }

        return module
    }

    private fun isModuleInfoHeader(header: Header) =
        moduleInfoPrefixes.any {
            header.filename.startsWith(it)
        }

    private fun parseModuleInfo(header: Header) =
        header.comment
            ?.let { mdParser.parse(it) }
            ?.let { moduleDocumentationParser.parse(it) }
}
