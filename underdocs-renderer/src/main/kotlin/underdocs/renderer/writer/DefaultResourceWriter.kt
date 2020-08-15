package underdocs.renderer.writer

import java.io.File
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes

class DefaultResourceWriter : ResourceWriter {
    override fun writeInternalResource(internalPath: String, outputPath: String) {
        val resourceStream = DefaultResourceWriter::class.java.getResourceAsStream(internalPath)

        createDirectoryStructure(outputPath)

        Files.copy(resourceStream, Paths.get(outputPath), StandardCopyOption.REPLACE_EXISTING)
    }

    override fun writeExternalResources(inputDirectory: String, outputDirectory: String) {
        val inputDirectoryPath = Paths.get(inputDirectory)
        val outputDirectoryPath = Paths.get(outputDirectory)

        Files.walkFileTree(
            inputDirectoryPath,
            object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                    if (file != null) {
                        val destinationPath = outputDirectoryPath.resolve(inputDirectoryPath.relativize(file))

                        createDirectoryStructure(destinationPath.toString())

                        Files.copy(file, destinationPath, StandardCopyOption.REPLACE_EXISTING)
                    }

                    return FileVisitResult.CONTINUE
                }
            }
        )
    }

    private fun createDirectoryStructure(path: String) {
        File(path).parentFile.mkdirs()
    }
}
