package underdocs.renderer.output.html.render.section

import j2html.TagCreator.code
import j2html.TagCreator.pre
import j2html.tags.Tag
import underdocs.renderer.representation.MacroConstant
import underdocs.renderer.representation.TypeSynonym
import underdocs.renderer.representation.Visitable
import underdocs.renderer.representation.visitor.BaseVisitor

class RepresentationRenderer: BaseVisitor() {
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

    private fun wrapIntoPreAndCode(source: String) = pre(
            code(
                    source
            ).withClass("language-c")
    )
}