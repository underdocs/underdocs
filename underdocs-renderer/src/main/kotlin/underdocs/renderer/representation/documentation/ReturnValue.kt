package underdocs.renderer.representation.documentation

data class ReturnValueItem(
    val value: String,
    val description: String
)

data class ReturnValue(
    val success: List<ReturnValueItem>,
    val error: List<ReturnValueItem>
) {
    fun isNotEmpty(): Boolean {
        return success.isNotEmpty() || error.isNotEmpty()
    }
}
