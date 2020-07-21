package underdocs.error.pretty

interface ErrorPrettyPrinter {
    fun print(exception: Exception): String
}