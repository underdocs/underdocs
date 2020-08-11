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

interface ElementVisitor {
    fun visit(element: EnumConstant)

    fun visit(element: EnumElement)

    fun visit(element: EnumMember)

    fun visit(element: EnumType)

    fun visit(element: Function)

    fun visit(element: FunctionParameter)

    fun visit(element: MacroConstant)

    fun visit(element: MacroFunction)

    fun visit(element: MacroParameter)

    fun visit(element: ReferredType)

    fun visit(element: Struct)

    fun visit(element: StructMember)

    fun visit(element: StructType)

    fun visit(element: TypeSynonym)

    fun visit(element: Union)

    fun visit(element: UnionMember)

    fun visit(element: UnionType)

    fun visit(element: Variable)

    fun visit(element: VariableMember)
}
