package underdocs.renderer.writer

import java.io.File
import java.lang.IllegalStateException
import java.nio.file.Files
import java.nio.file.Paths

class DirectoryCreatingOutputWriter: OutputWriter {
    override fun write(contents: String, path: String) {
        val actualPath = Paths.get(path)

        val directoryPath = actualPath.parent.toString()

        if (!File(directoryPath).mkdirs()) {
            throw IllegalStateException("Could not create directories to path: ${path}")
        }

        Files.writeString(actualPath, contents)
    }
}