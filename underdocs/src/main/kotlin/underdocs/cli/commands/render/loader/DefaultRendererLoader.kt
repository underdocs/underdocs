package underdocs.cli.commands.render.loader

import underdocs.common.renderer.RendererFactory
import java.io.FileInputStream
import java.net.URLClassLoader
import java.nio.file.Paths
import java.util.jar.JarInputStream

class DefaultRendererLoader: RendererLoader {
    companion object {
        private const val RENDERER_FACTORY_PROPERTY = "Renderer-Factory"
    }

    override fun loadFromJar(jarPath: String): RendererFactory<Any> {
        val factoryClassName = getFactoryClassName(jarPath)

        val rendererFactoryClass = loadFactoryClass(jarPath, factoryClassName)

        return rendererFactoryClass.newInstance()
    }

    private fun getFactoryClassName(jarPath: String): String {
        val fileInputStream = FileInputStream(jarPath)

        fileInputStream.use {
            val jarInputStream = JarInputStream(FileInputStream(jarPath))

            jarInputStream.use {
                val attributes = jarInputStream.manifest.mainAttributes

                return attributes.getValue(RENDERER_FACTORY_PROPERTY)
            }
        }
    }
    private fun loadFactoryClass(jarPath: String, factoryClassName: String): Class<RendererFactory<Any>> {
        val jarUrl = Paths.get(jarPath).toUri().toURL()

        val classLoader = URLClassLoader(
                arrayOf(jarUrl),
                this.javaClass.classLoader
        )

        @Suppress("UNCHECKED_CAST")
        return classLoader.loadClass(factoryClassName) as Class<RendererFactory<Any>>
    }
}