package underdocs.renderer.writer

interface ResourceWriter {
    fun writeInternalResource(internalPath: String, outputPath: String)

    fun writeExternalResources(inputDirectory: String, outputDirectory: String)
}
