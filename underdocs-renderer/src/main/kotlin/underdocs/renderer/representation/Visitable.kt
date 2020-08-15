package underdocs.renderer.representation

import underdocs.renderer.representation.visitor.Visitor

interface Visitable {
    fun accept(visitor: Visitor)
}
