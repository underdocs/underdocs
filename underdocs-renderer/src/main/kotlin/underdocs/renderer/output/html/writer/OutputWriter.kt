package underdocs.renderer.output.html.writer

interface OutputWriter {
    fun write(contents: String, path: String)
}