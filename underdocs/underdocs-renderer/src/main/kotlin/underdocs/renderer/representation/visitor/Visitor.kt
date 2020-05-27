package underdocs.renderer.representation.visitor

import underdocs.renderer.representation.EnumConstant
import underdocs.renderer.representation.EnumElement
import underdocs.renderer.representation.EnumMember
import underdocs.renderer.representation.EnumType
import underdocs.renderer.representation.Function
import underdocs.renderer.representation.FunctionParameter
import underdocs.renderer.representation.Header
import underdocs.renderer.representation.MacroConstant
import underdocs.renderer.representation.MacroFunction
import underdocs.renderer.representation.MacroParameter
import underdocs.renderer.representation.Module
import underdocs.renderer.representation.ReferredType
import underdocs.renderer.representation.Struct
import underdocs.renderer.representation.StructMember
import underdocs.renderer.representation.StructType
import underdocs.renderer.representation.TypeSynonym
import underdocs.renderer.representation.Union
import underdocs.renderer.representation.UnionMember
import underdocs.renderer.representation.UnionType
import underdocs.renderer.representation.Variable
import underdocs.renderer.representation.VariableMember

interface Visitor {
    fun accept(enumConstant: EnumConstant) = Unit

    fun accept(enumElement: EnumElement) = Unit

    fun accept(enumMember: EnumMember) = Unit

    fun accept(enumType: EnumType) = Unit

    fun accept(function: Function) = Unit

    fun accept(functionParameter: FunctionParameter) = Unit

    fun accept(header: Header) = Unit

    fun accept(macroConstant: MacroConstant) = Unit

    fun accept(macroFunction: MacroFunction) = Unit

    fun accept(macroParameter: MacroParameter) = Unit

    fun accept(module: Module) = Unit

    fun accept(referredType: ReferredType) = Unit

    fun accept(struct: Struct) = Unit

    fun accept(structMember: StructMember) = Unit

    fun accept(structType: StructType) = Unit

    fun accept(typeSynonym: TypeSynonym) = Unit

    fun accept(union: Union) = Unit

    fun accept(unionMember: UnionMember) = Unit

    fun accept(unionType: UnionType) = Unit

    fun accept(variable: Variable) = Unit

    fun accept(variableMember: VariableMember) = Unit
}