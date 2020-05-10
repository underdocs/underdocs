package underdocs.renderer.representation

interface Element : Visitable {
    fun getStartingLine(): Int

    fun getRawRepresentation(): String
}
