package underdocs.renderer.writer

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class DirectoryCreatingOutputWriter : OutputWriter {
    override fun write(contents: String, path: String) {
        val actualPath = Paths.get(path)

        val directoryPath = actualPath.parent.toString()

        File(directoryPath).mkdirs()

        Files.writeString(actualPath, contents)
    }
}