package underdocs.error.reporter

interface ErrorReporter {
    fun report(exception: Exception)
}
