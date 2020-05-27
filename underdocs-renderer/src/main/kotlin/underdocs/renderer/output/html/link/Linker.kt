package underdocs.renderer.output.html.link

import underdocs.renderer.representation.Visitable

interface Linker {
    fun localFileOutputPathToVisitable(obj: Visitable): String

    fun siteLinkToVisitable(obj: Visitable): String

    fun siteLinkBetween(from: Visitable, to: Visitable): String

    fun siteLinkBetween(from: Visitable, to: String): String
    fun remoteLinkToTag(tag: String): String?
    fun remoteLinkToVisitable(visitable: Visitable): String?
}
