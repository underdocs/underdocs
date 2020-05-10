package underdocs.renderer.output.html.render.misc

import underdocs.renderer.representation.Header
import underdocs.renderer.representation.Module
import underdocs.renderer.representation.visitor.BaseVisitor
import java.io.File

class NameVisitor : BaseVisitor() {
    var name: String? = null

    override fun accept(header: Header) {
        name = header.filename
    }

    override fun accept(module: Module) {
        name = module.documentation?.title
                ?: module.path.split(File.separator).lastOrNull()
                        ?: "root"
    }
}