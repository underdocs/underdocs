package underdocs.representation.visitor

import underdocs.representation.EnumConstant
import underdocs.representation.EnumElement
import underdocs.representation.EnumMember
import underdocs.representation.EnumType
import underdocs.representation.Function
import underdocs.representation.FunctionParameter
import underdocs.representation.MacroConstant
import underdocs.representation.MacroFunction
import underdocs.representation.MacroParameter
import underdocs.representation.ReferredType
import underdocs.representation.Struct
import underdocs.representation.StructMember
import underdocs.representation.StructType
import underdocs.representation.TypeSynonym
import underdocs.representation.Union
import underdocs.representation.UnionMember
import underdocs.representation.UnionType
import underdocs.representation.Variable
import underdocs.representation.VariableMember

open class BaseElementVisitor : ElementVisitor {
    override fun visit(element: EnumConstant) = Unit

    override fun visit(element: EnumElement) = Unit

    override fun visit(element: EnumMember) = Unit

    override fun visit(element: EnumType) = Unit

    override fun visit(element: Function) = Unit

    override fun visit(element: FunctionParameter) = Unit

    override fun visit(element: MacroConstant) = Unit

    override fun visit(element: MacroFunction) = Unit

    override fun visit(element: MacroParameter) = Unit

    override fun visit(element: ReferredType) = Unit

    override fun visit(element: Struct) = Unit

    override fun visit(element: StructMember) = Unit

    override fun visit(element: StructType) = Unit

    override fun visit(element: TypeSynonym) = Unit

    override fun visit(element: Union) = Unit

    override fun visit(element: UnionMember) = Unit

    override fun visit(element: UnionType) = Unit

    override fun visit(element: Variable) = Unit

    override fun visit(element: VariableMember) = Unit
}
