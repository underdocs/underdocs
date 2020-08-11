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

class ComplexMemberRenderer {
    private val representationBuilder: StringBuilder = StringBuilder()

    fun render(members: List<Member>, initialDepth: Int): String {
        representationBuilder.clear()

        if (members.isNotEmpty()) {
            renderMembers(members, initialDepth)
        }

        return representationBuilder.toString()
    }

    fun render(type: Type, initialDepth: Int): String {
        representationBuilder.clear()

        renderType(type, initialDepth)

        return representationBuilder.toString()
    }

    private fun renderMembers(members: List<Member>, depth: Int) {
        val indentation = indentationOfDepth(depth)

        members.take(members.size - 1).forEach {
            representationBuilder.append(indentation)
            renderMember(it, depth + 2)
            representationBuilder.append(";\n")
        }

        representationBuilder.append(indentation)
        members.last().let { renderMember(it, depth + 2) }
        representationBuilder.append(";")
    }

    private fun renderMember(member: Member, depth: Int) = when (member) {
        is EnumMember -> renderEnumMember(member, depth)
        is StructMember -> renderStructMember(member, depth)
        is UnionMember -> renderUnionMember(member, depth)
        is VariableMember -> renderVariableMember(member, depth)
        else -> Unit
    }

    private fun renderEnumMember(member: EnumMember, depth: Int) {
        val leadingSpaces = " ".repeat(depth)
        val leadingMemberSpaces = " ".repeat(depth)

        var name = member.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("enum $name{\n")

        val enumMembers = member.members
            .map { enumMember ->
                if (enumMember.value == null) {
                    "${leadingMemberSpaces}${enumMember.name}"
                } else {
                    "${leadingMemberSpaces}${enumMember.name} = ${enumMember.value}"
                }
            }
            .joinToString(",\n")

        representationBuilder.append(enumMembers)

        representationBuilder.append("\n$leadingSpaces}")
    }

    private fun renderStructMember(member: StructMember, depth: Int) {
        val leadingSpaces = " ".repeat(depth - 2)

        var name = member.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("struct $name{\n")

        renderMembers(member.members, depth)

        representationBuilder.append("\n$leadingSpaces}")
    }

    private fun renderUnionMember(member: UnionMember, depth: Int) {
        val leadingSpaces = " ".repeat(depth - 2)

        var name = member.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("union $name{\n")

        renderMembers(member.members, depth)

        representationBuilder.append("\n$leadingSpaces}")
    }

    private fun renderVariableMember(member: VariableMember, depth: Int) {
        val leadingSpaces = " ".repeat(depth - 2)

        var specifiers = member.specifiers.joinToString(" ")

        if (specifiers.isNotEmpty()) {
            specifiers += " "
        }

        representationBuilder.append(specifiers)

        renderType(member.type, depth)

        representationBuilder.append(" ${member.name ?: ""}")
    }

    private fun renderType(type: Type, depth: Int) = when (type) {
        is EnumType -> renderEnumType(type, depth)
        is ReferredType -> renderReferredType(type, depth)
        is StructType -> renderStructType(type, depth)
        is UnionType -> renderUnionType(type, depth)
        else -> Unit
    }

    private fun renderUnionType(type: UnionType, depth: Int) {
        val leadingSpaces = " ".repeat(depth - 2)

        var name = type.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("union $name{\n")

        renderMembers(type.members, depth)

        representationBuilder.append("\n$leadingSpaces}")
    }

    private fun renderStructType(type: StructType, depth: Int) {
        val leadingSpaces = " ".repeat(depth - 2)

        var name = type.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("struct $name{\n")

        renderMembers(type.members, depth)

        representationBuilder.append("\n$leadingSpaces}")
    }

    private fun renderEnumType(type: EnumType, depth: Int) {
        val leadingSpaces = " ".repeat(depth - 2)
        val leadingMemberSpaces = " ".repeat(depth)

        var name = type.name ?: ""

        if (name.isNotEmpty()) {
            name += " "
        }

        representationBuilder.append("enum $name{\n")

        val enumMembers = type.members
            .map { enumMember ->
                if (enumMember.value == null) {
                    "${leadingMemberSpaces}${enumMember.name}"
                } else {
                    "${leadingMemberSpaces}${enumMember.name} = ${enumMember.value}"
                }
            }
            .joinToString(",\n")

        representationBuilder.append(enumMembers)

        representationBuilder.append("\n$leadingSpaces}")
    }

    private fun renderReferredType(type: ReferredType, depth: Int) {
        var specifiers = type.specifiers.joinToString(" ")

        if (specifiers.isNotEmpty()) {
            specifiers += " "
        }

        representationBuilder.append("$specifiers${type.name}")
    }

    private fun indentationOfDepth(depth: Int) = " ".repeat(depth)
}
