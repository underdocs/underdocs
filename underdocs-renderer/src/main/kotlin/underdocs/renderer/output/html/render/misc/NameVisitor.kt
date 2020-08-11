package underdocs.renderer.output.html.render.misc

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

    override fun accept(macroConstant: MacroConstant) {
        name = macroConstant.name
    }

    override fun accept(typeSynonym: TypeSynonym) {
        name = typeSynonym.newName
    }

    override fun accept(enumElement: EnumElement) {
        name = enumElement.name ?: "anonymous enum"
    }

    override fun accept(macroFunction: MacroFunction) {
        name = macroFunction.name
    }

    override fun accept(struct: Struct) {
        name = if (struct.name != null && struct.name.isNotEmpty()) {
            struct.name
        } else {
            "anonymous struct"
        }
    }

    override fun accept(union: Union) {
        name = if (union.name != null && union.name.isNotEmpty()) {
            union.name
        } else {
            "anonymous union"
        }
    }

    override fun accept(function: Function) {
        name = function.name
    }

    override fun accept(variable: Variable) {
        name = variable.name
    }
}
