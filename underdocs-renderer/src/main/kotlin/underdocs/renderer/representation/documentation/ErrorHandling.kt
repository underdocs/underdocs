package underdocs.renderer.representation.documentation

data class ErrorHandlingItem(
    val state: String,
    val condition: String
)

data class ErrorHandling (
    val errorHandlingItems: List<ErrorHandlingItem>
) {
    fun isNotEmpty(): Boolean {
        return errorHandlingItems.isNotEmpty()
    }
}
