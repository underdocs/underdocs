package underdocs.renderer

import underdocs.representation.Codebase

class CppReferenceRenderer(private val configuration: _root_ide_package_.underdocs.renderer.CppReferenceRendererConfiguration): _root_ide_package_.underdocs.renderer.CodebaseRenderer {
    override fun render(codebase: Codebase) {
        val codebaseParser = _root_ide_package_.underdocs.renderer.parser.CodebaseParser.create()

        val topLevelModule = codebaseParser.parseHeaders(codebase.headers)

        val htmlRenderer = _root_ide_package_.underdocs.renderer.output.html.CppReferenceHtmlRenderer.getInstance(topLevelModule)

        println(topLevelModule)
    }
}