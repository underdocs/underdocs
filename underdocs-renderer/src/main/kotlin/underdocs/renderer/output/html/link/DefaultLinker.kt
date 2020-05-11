package underdocs.renderer.output.html.link

import underdocs.configuration.RendererConfiguration
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

class DefaultLinker(private val configuration: RendererConfiguration, codebaseBaseDirectory: String) : Linker {
    private val codebaseBasePath = Paths.get(codebaseBaseDirectory)
    private val outputPath = Paths.get(configuration.outputDirectory)

    private val outputPathVisitor = OutputPathVisitor()

    override fun remoteLinkToTag(tag: String) =
            configuration.remoteRepositoryTagLinkTemplate?.replace("\${tag}", tag)

    override fun remoteLinkToVisitable(visitable: Visitable) =
            configuration.remoteRepositoryLineLinkTemplate?.let { template ->
                val remoteLinkVisitor = RemoteLinkVisitor(template)

                return@let remoteLinkVisitor.linkTo(visitable)
            }

    override fun localFileOutputPathToVisitable(obj: Visitable): String {
        obj.accept(outputPathVisitor)

        return outputPathVisitor.getPath()
    }

    override fun siteLinkToVisitable(obj: Visitable): String {
        val visitableFileSystemPath = Paths.get(this.localFileOutputPathToVisitable(obj))

        val relativeFileSystemPathString = outputPath.relativize(visitableFileSystemPath)
                .toString()

        return relativeFileSystemPathString.replace('\\', '/')
    }

    override fun siteLinkBetween(from: Visitable, to: Visitable): String {
        val fromLink = this.siteLinkToVisitable(from)
        val toLink = this.siteLinkToVisitable(to)

        return relativeLinkBetween(fromLink, toLink)
    }

    override fun siteLinkBetween(from: Visitable, to: String): String {
        val fromLink = this.siteLinkToVisitable(from)

        return relativeLinkBetween(fromLink, to)
    }

    private fun relativeLinkBetween(fromLink: String, toLink: String): String {
        val fromLinkSegments = fromLink.split("/")
        val toLinkSegments = toLink.split("/")

        val matchingSegmentCount = fromLinkSegments
                .zip(toLinkSegments)
                .takeWhile { (from, to) -> from == to }
                .count()

        // Minus one, because the filename segment is not considered.
        val upSegmentCount = fromLinkSegments.size - 1 - matchingSegmentCount

        return "../".repeat(upSegmentCount) + toLinkSegments.drop(matchingSegmentCount).joinToString("/")
    }

    private fun localRepositoryRelativePathAsPath(path: String) =
            codebaseBasePath.relativize(Paths.get(path))

    private fun localRepositoryRelativePathAsString(path: String) =
            localRepositoryRelativePathAsPath(path)
                    .toString()
                    .replace('\\', '/')

    private inner class RemoteLinkVisitor(private val template: String) : BaseVisitor() {
        private var link: String? = null

        fun linkTo(visitable: Visitable): String {
            visitable.accept(this)

            return link!!
        }

        override fun accept(header: Header) {
            link = template
                    .replace("\${path}", localRepositoryRelativePathAsString(header.path))
                    .replace("\${line}", "")
        }

        override fun accept(module: Module) {
            link = template
                    .replace("\${path}", localRepositoryRelativePathAsString(module.path))
                    .replace("\${line}", "")
        }

        override fun accept(macroConstant: MacroConstant) {
            link = template
                    .replace("\${path}", localRepositoryRelativePathAsString(macroConstant.getParent()!!.path))
                    .replace("\${line}", macroConstant.getStartingLine().toString())
        }

        override fun accept(typeSynonym: TypeSynonym) {
            link = template
                    .replace("\${path}", localRepositoryRelativePathAsString(typeSynonym.getParent()!!.path))
                    .replace("\${line}", typeSynonym.getStartingLine().toString())
        }

        override fun accept(enumElement: EnumElement) {
            link = template
                    .replace("\${path}", localRepositoryRelativePathAsString(enumElement.getParent()!!.path))
                    .replace("\${line}", enumElement.getStartingLine().toString())
        }
    }

    private inner class OutputPathVisitor : BaseVisitor() {
        private var path: Path? = null

        fun getPath(): String {
            val resolvedPath = outputPath.resolve(path!!)

            return "$resolvedPath.html"
        }

        override fun accept(enumElement: EnumElement) {
            val parentPath = localRepositoryRelativePathAsPath(enumElement.getParent()?.path!!)

            path = parentPath
                    .resolve(enumElement.name ?: "unnamed-enum")
        }

        override fun accept(function: Function) {
            val parentPath = localRepositoryRelativePathAsPath(function.getParent()?.path!!)

            path = parentPath
                    .resolve(function.name)
        }

        override fun accept(header: Header) {
            path = localRepositoryRelativePathAsPath(header.path)
        }

        override fun accept(macroConstant: MacroConstant) {
            val parentPath = localRepositoryRelativePathAsPath(macroConstant.getParent()?.path!!)

            path = parentPath
                    .resolve(macroConstant.name)
        }

        override fun accept(macroFunction: MacroFunction) {
            val parentPath = localRepositoryRelativePathAsPath(macroFunction.getParent()?.path!!)

            path = parentPath
                    .resolve(macroFunction.name)
        }

        override fun accept(module: Module) {
            val codebaseRelativePath = localRepositoryRelativePathAsPath(module.path)

            path = codebaseRelativePath.resolve("index")
        }

        override fun accept(struct: Struct) {
            val parentPath = localRepositoryRelativePathAsPath(struct.getParent()?.path!!)

            path = parentPath
                    .resolve(struct.name)
        }

        override fun accept(typeSynonym: TypeSynonym) {
            val parentPath = localRepositoryRelativePathAsPath(typeSynonym.getParent()?.path!!)

            path = parentPath
                    .resolve(typeSynonym.newName)
        }

        override fun accept(union: Union) {
            val parentPath = localRepositoryRelativePathAsPath(union.getParent()?.path!!)

            path = parentPath
                    .resolve(union.name)
        }

        override fun accept(variable: Variable) {
            val parentPath = localRepositoryRelativePathAsPath(variable.getParent()?.path!!)

            path = parentPath
                    .resolve(variable.name)
        }
    }
}