package underdocs.renderer.representation

interface TopLevelElement : Element {
    fun getParent(): Header?

    fun setParent(header: Header)
}
