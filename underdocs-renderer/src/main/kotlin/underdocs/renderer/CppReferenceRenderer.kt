package underdocs.renderer

import underdocs.configuration.RendererConfiguration
import underdocs.renderer.output.html.CppReferenceHtmlRenderer
import underdocs.renderer.parser.CodebaseParser
import underdocs.representation.Codebase

class CppReferenceRenderer(private val configuration: RendererConfiguration): CodebaseRenderer {
    override fun render(codebase: Codebase) {
        val codebaseParser = CodebaseParser.create()

        val topLevelModule = codebaseParser.parseHeaders(codebase.headers)

        val htmlRenderer = CppReferenceHtmlRenderer.getInstance(topLevelModule)

        println(topLevelModule)
    }
}