package underdocs.renderer.writer

interface OutputWriter {
    fun write(contents: String, path: String)
}
