package underdocs.renderer.output.html.render.section

import underdocs.renderer.representation.EnumMember
import underdocs.renderer.representation.EnumType
import underdocs.renderer.representation.Member
import underdocs.renderer.representation.ReferredType
import underdocs.renderer.representation.StructMember
import underdocs.renderer.representation.StructType
import underdocs.renderer.representation.Type
import underdocs.renderer.representation.UnionMember
import underdocs.renderer.representation.UnionType
import underdocs.renderer.representation.VariableMember

class SingleLineTypeRenderer {
    private val representationBuilder: StringBuilder = StringBuilder()

    fun render(type: Type): String {
        representationBuilder.clear()

        renderType(type)

        return representationBuilder.toString()
    }


    fun render(members: List<Member>): String {
        representationBuilder.clear()

        if (members.isNotEmpty()) {
            renderMembers(members)
        }

        return representationBuilder.toString()
    }

    private fun renderMembers(members: List<Member>) {
        members.forEach {
            representationBuilder.append(" ")
            renderMember(it)
            representationBuilder.append("; ")
        }
    }

    private fun renderMember(member: Member) = when (member) {
        is EnumMember -> renderEnumMember(member)
        is StructMember -> renderStructMember(member)
        is UnionMember -> renderUnionMember(member)
        is VariableMember -> renderVariableMember(member)
        else -> Unit
    }

    private fun renderEnumMember(member: EnumMember) {
        var name = member.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("enum $name{ ")

        val enumMembers = member.members
                .map { enumMember ->
                    if (enumMember.value == null) {
                        enumMember.name
                    } else {
                        "${enumMember.name} = ${enumMember.value}"
                    }
                }
                .joinToString(", ")

        representationBuilder.append(enumMembers)

        representationBuilder.append(" }")
    }

    private fun renderStructMember(member: StructMember) {
        var name = member.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("struct $name{ ")

        renderMembers(member.members)

        representationBuilder.append(" }")
    }

    private fun renderUnionMember(member: UnionMember) {
        var name = member.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("union $name{ ")

        renderMembers(member.members)

        representationBuilder.append(" }")
    }

    private fun renderVariableMember(member: VariableMember) {
        var specifiers = member.specifiers.joinToString(" ")

        if (specifiers.isNotEmpty()) {
            specifiers += " "
        }

        representationBuilder.append(specifiers)

        renderType(member.type)

        representationBuilder.append(" ${member.name ?: ""}")
    }

    private fun renderType(type: Type) = when (type) {
        is EnumType -> renderEnumType(type)
        is ReferredType -> renderReferredType(type)
        is StructType -> renderStructType(type)
        is UnionType -> renderUnionType(type)
        else -> Unit
    }

    private fun renderUnionType(type: UnionType) {
        var name = type.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("union $name{ ")

        renderMembers(type.members)

        representationBuilder.append(" }")
    }

    private fun renderStructType(type: StructType) {
        var name = type.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("struct $name{ ")

        renderMembers(type.members)

        representationBuilder.append(" }")
    }

    private fun renderEnumType(type: EnumType) {
        var name = type.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("enum $name{ ")

        val enumMembers = type.members
                .map { enumMember ->
                    if (enumMember.value == null) {
                        enumMember.name
                    } else {
                        "${enumMember.name} = ${enumMember.value}"
                    }
                }
                .joinToString(", ")

        representationBuilder.append(enumMembers)

        representationBuilder.append(" }")
    }

    private fun renderReferredType(type: ReferredType) {
        var specifiers = type.specifiers.joinToString(" ")

        if (specifiers.isNotEmpty()) {
            specifiers += " "
        }

        representationBuilder.append("$specifiers${type.name}")
    }
}