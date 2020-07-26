package underdocs.error.pretty

import underdocs.error.BaseUnderdocsError

class DefaultErrorPrettyPrinter : ErrorPrettyPrinter {
    override fun print(exception: Exception) = if (exception is BaseUnderdocsError) {
        printBaseUnderdocsException(exception)
    } else {
        printUnexpectedException(exception)
    }

    private fun printBaseUnderdocsException(exception: BaseUnderdocsError) = """
        |${exception.title.toUpperCase()} - Error Code ${exception.code}
        |Details
        |${exception.getDescription()}
        |
        |Possible solution
        |${exception.getSolution()}
    """.trimMargin()


    private fun printUnexpectedException(exception: Exception) = """
        |Unexpected exception ${exception.javaClass.name}
        |${exception.message}
        |
        |Stacktrace
        |${exception.stackTrace.joinToString("\n\t", prefix = "\t")}
    """.trimMargin()
}