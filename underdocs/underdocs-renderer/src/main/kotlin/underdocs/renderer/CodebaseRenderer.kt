package underdocs.renderer

import underdocs.configuration.RendererConfiguration
import underdocs.representation.Codebase

interface CodebaseRenderer {
    companion object {
        fun create(configuration: RendererConfiguration): CodebaseRenderer = CppReferenceRenderer(configuration)
    }

    fun render(codebase: Codebase);
}