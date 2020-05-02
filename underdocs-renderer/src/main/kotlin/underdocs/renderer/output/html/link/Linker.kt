package underdocs.renderer.output.html.link

import underdocs.renderer.representation.Visitable

interface Linker {
    fun outputPathFor(obj: Visitable): String

    fun linkTo(obj: Visitable): String

    fun linkBetween(from: Visitable, to: Visitable): String

    fun linkBetween(from: Visitable, to: String): String
}
