package underdocs.cli.commands.render.loader

import underdocs.common.renderer.RendererFactory

interface RendererLoader {
    fun loadFromJar(jarPath: String): RendererFactory<Any>
}
