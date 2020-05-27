package underdocs.representation

import underdocs.representation.visitor.ElementVisitor

interface Element {
    fun getStartingLine(): Int

    fun getRaw(): String

    fun accept(visitor: ElementVisitor)
}
