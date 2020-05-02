package underdocs.renderer.output.html.link

import underdocs.renderer.representation.EnumElement
import underdocs.renderer.representation.Function
import underdocs.renderer.representation.Header
import underdocs.renderer.representation.MacroConstant
import underdocs.renderer.representation.MacroFunction
import underdocs.renderer.representation.Module
import underdocs.renderer.representation.Struct
import underdocs.renderer.representation.TypeSynonym
import underdocs.renderer.representation.Union
import underdocs.renderer.representation.Variable
import underdocs.renderer.representation.Visitable
import underdocs.renderer.representation.visitor.BaseVisitor
import java.nio.file.Path
import java.nio.file.Paths

class DefaultLinker(codebaseBaseDirectory: String, outputDirectory: String): Linker {
    private val codebaseBasePath = Paths.get(codebaseBaseDirectory)
    private val outputPath = Paths.get(outputDirectory)

    private val outputPathVisitor = OutputPathVisitor()

    override fun outputPathFor(obj: Visitable): String {
        obj.accept(outputPathVisitor)

        return outputPathVisitor.getPath()
    }

    override fun linkTo(obj: Visitable): String {
        val visitableFileSystemPath = Paths.get(outputPathFor(obj))

        val relativeFileSystemPathString = outputPath.relativize(visitableFileSystemPath)
                .toString()

        return "/" + relativeFileSystemPathString.replace('\\', '/')
    }

    private fun removeCodebaseBasePath(path: String) =
            Paths.get(path).relativize(codebaseBasePath)

    private inner class OutputPathVisitor : BaseVisitor() {
        private var path: Path? = null

        fun getPath(): String {
            return outputPath.resolve(path!!).toString() + ".html"
        }

        override fun accept(enumElement: EnumElement) {
            val parentPath = removeCodebaseBasePath(enumElement.getParent()?.path!!)

            path = outputPath
                    .resolve(parentPath)
                    .resolve(enumElement.name!!)
        }

        override fun accept(function: Function) {
            val parentPath = removeCodebaseBasePath(function.getParent()?.path!!)

            path = outputPath
                    .resolve(parentPath)
                    .resolve(function.name)
        }

        override fun accept(header: Header) {
            path = removeCodebaseBasePath(header.path)
        }

        override fun accept(macroConstant: MacroConstant) {
            val parentPath = removeCodebaseBasePath(macroConstant.getParent()?.path!!)

            path = outputPath
                    .resolve(parentPath)
                    .resolve(macroConstant.name)
        }

        override fun accept(macroFunction: MacroFunction) {
            val parentPath = removeCodebaseBasePath(macroFunction.getParent()?.path!!)

            path = outputPath
                    .resolve(parentPath)
                    .resolve(macroFunction.name)
        }

        override fun accept(module: Module) {
            path = removeCodebaseBasePath(module.path)
        }

        override fun accept(struct: Struct) {
            val parentPath = removeCodebaseBasePath(struct.getParent()?.path!!)

            path = outputPath
                    .resolve(parentPath)
                    .resolve(struct.name)
        }

        override fun accept(typeSynonym: TypeSynonym) {
            val parentPath = removeCodebaseBasePath(typeSynonym.getParent()?.path!!)

            path = outputPath
                    .resolve(parentPath)
                    .resolve(typeSynonym.newName)
        }

        override fun accept(union: Union) {
            val parentPath = removeCodebaseBasePath(union.getParent()?.path!!)

            path = outputPath
                    .resolve(parentPath)
                    .resolve(union.name)
        }

        override fun accept(variable: Variable) {
            val parentPath = removeCodebaseBasePath(variable.getParent()?.path!!)

            path = outputPath
                    .resolve(parentPath)
                    .resolve(variable.name)
        }
    }
}