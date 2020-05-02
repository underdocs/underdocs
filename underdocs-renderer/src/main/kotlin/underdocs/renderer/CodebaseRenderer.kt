package underdocs.renderer

import underdocs.representation.Codebase

interface CodebaseRenderer {
    companion object {
        fun create(configuration: _root_ide_package_.underdocs.renderer.CppReferenceRendererConfiguration): _root_ide_package_.underdocs.renderer.CodebaseRenderer = _root_ide_package_.underdocs.renderer.CppReferenceRenderer(configuration)
    }

    fun render(codebase: Codebase);
}