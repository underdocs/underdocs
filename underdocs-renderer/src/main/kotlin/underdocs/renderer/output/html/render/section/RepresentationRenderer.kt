package underdocs.renderer.output.html.render.section

import j2html.TagCreator.code
import j2html.TagCreator.pre
import j2html.tags.Tag
import underdocs.renderer.representation.EnumElement
import underdocs.renderer.representation.MacroConstant
import underdocs.renderer.representation.MacroFunction
import underdocs.renderer.representation.Struct
import underdocs.renderer.representation.TypeSynonym
import underdocs.renderer.representation.Union
import underdocs.renderer.representation.Visitable
import underdocs.renderer.representation.visitor.BaseVisitor

class RepresentationRenderer: BaseVisitor() {
    private val memberRenderer = ComplexMemberRenderer()

    private var source: String? = null

    fun render(visitable: Visitable): Tag<*> {
        visitable.accept(this)

        return wrapIntoPreAndCode(source!!)
    }

    override fun accept(macroConstant: MacroConstant) {
        source = "#define ${macroConstant.name} ${macroConstant.expansion}"
    }

    override fun accept(typeSynonym: TypeSynonym) {
        source = "typedef ${typeSynonym.originalName} ${typeSynonym.newName}"
    }

    override fun accept(enumElement: EnumElement) {
        var specifiers = enumElement.specifiers.joinToString(" ")

        if (specifiers.isNotEmpty()) {
            specifiers += " "
        }

        val members = enumElement.members
                .map { member ->
                    if (member.value != null) {
                        "  ${member.name} = ${member.value}"
                    } else {
                        "  ${member.name}"
                    }
                }
                .joinToString(",\n")

        source  = if (enumElement.typedef) {
            "typedef enum {\n${members}\n} ${enumElement.name}"
        } else {
            var name = enumElement.name ?: ""

            if (name.isNotEmpty()) {
                name += " "
            }

            "${specifiers}enum $name{\n${members}\n}"
        }
    }

    override fun accept(macroFunction: MacroFunction) {
        val parameters = macroFunction.parameters.joinToString(", ")

        source = "#define ${macroFunction.name} ($parameters) ${macroFunction.expansion}"
    }

    override fun accept(struct: Struct) {
        var specifiers = struct.specifiers.joinToString(" ")

        if (specifiers.isNotEmpty()) {
            specifiers += " "
        }

        val members = memberRenderer.render(struct.members, 2)

        source  = if (struct.typedef) {
            "typedef struct {\n${members}\n} ${struct.name}"
        } else {
            var name = struct.name ?: ""

            if (name.isNotEmpty()) {
                name += " "
            }

            "${specifiers}struct $name{\n${members}\n}"
        }
    }

    override fun accept(union: Union) {
        var specifiers = union.specifiers.joinToString(" ")

        if (specifiers.isNotEmpty()) {
            specifiers += " "
        }

        val members = memberRenderer.render(union.members, 2)

        source  = if (union.typedef) {
            "typedef union {\n${members}\n} ${union.name}"
        } else {
            var name = union.name ?: ""

            if (name.isNotEmpty()) {
                name += " "
            }

            "${specifiers}union $name{\n${members}\n}"
        }
    }

    private fun wrapIntoPreAndCode(source: String) = pre(
            code(
                    source
            ).withClass("language-c")
    )
}