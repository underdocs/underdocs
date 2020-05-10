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

open class BaseVisitor : Visitor {
    override fun accept(enumConstant: EnumConstant) = Unit

    override fun accept(enumElement: EnumElement) = Unit

    override fun accept(enumMember: EnumMember) = Unit

    override fun accept(enumType: EnumType) = Unit

    override fun accept(function: Function) = Unit

    override fun accept(functionParameter: FunctionParameter) = Unit

    override fun accept(header: Header) = Unit

    override fun accept(macroConstant: MacroConstant) = Unit

    override fun accept(macroFunction: MacroFunction) = Unit

    override fun accept(macroParameter: MacroParameter) = Unit

    override fun accept(module: Module) = Unit

    override fun accept(referredType: ReferredType) = Unit

    override fun accept(struct: Struct) = Unit

    override fun accept(structMember: StructMember) = Unit

    override fun accept(structType: StructType) = Unit

    override fun accept(typeSynonym: TypeSynonym) = Unit

    override fun accept(union: Union) = Unit

    override fun accept(unionMember: UnionMember) = Unit

    override fun accept(unionType: UnionType) = Unit

    override fun accept(variable: Variable) = Unit

    override fun accept(variableMember: VariableMember) = Unit
}
