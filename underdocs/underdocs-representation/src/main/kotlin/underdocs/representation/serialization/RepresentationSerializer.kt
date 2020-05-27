package underdocs.representation.serialization

import underdocs.representation.Codebase
import java.nio.file.Path

interface RepresentationSerializer {
    companion object {
        fun create(): RepresentationSerializer = JacksonRepresentationSerializer()
    }

    fun readFrom(path: Path): Codebase

    fun writeInto(path: Path, codebase: Codebase)
}
