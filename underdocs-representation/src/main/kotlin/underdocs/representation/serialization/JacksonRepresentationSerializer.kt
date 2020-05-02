package underdocs.representation.serialization

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import underdocs.representation.Codebase
import underdocs.representation.Element
import underdocs.representation.Member
import underdocs.representation.Type
import java.nio.file.Path

class JacksonRepresentationSerializer: RepresentationSerializer {
    private val mapper: ObjectMapper

    init {
        mapper = ObjectMapper()
                .registerModule(KotlinModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .addMixIn(Element::class.java, ElementMixin::class.java)
                .addMixIn(Member::class.java, MemberMixin::class.java)
                .addMixIn(Type::class.java, TypeMixin::class.java)
    }

    override fun readFrom(path: Path): Codebase =
            mapper.readValue(path.toFile(), Codebase::class.java)

    override fun writeInto(path: Path, codebase: Codebase) =
            mapper.writeValue(path.toFile(), codebase)

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY
    )
    private abstract class ElementMixin

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY
    )
    private abstract class MemberMixin

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY
    )
    private abstract class TypeMixin
}