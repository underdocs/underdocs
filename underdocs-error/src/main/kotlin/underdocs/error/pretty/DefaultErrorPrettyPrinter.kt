package underdocs.error.pretty

import underdocs.error.BaseUnderdocsError

class DefaultErrorPrettyPrinter : ErrorPrettyPrinter {
    override fun print(exception: Exception): String {
        var prettyPrintedError = """"""

        if (exception is BaseUnderdocsError) {
            prettyPrintedError = """
                |${exception.title.toUpperCase()} - Error Code ${exception.code}
                |Details
                |${exception.getDescription()}
                |
                |Possible solution
                |${exception.getSolution()}
            """.trimMargin()

            if (exception.cause != null) prettyPrintedError += "\nError cause:\n${exception.cause?.message}"
        } else {
            val errorStackTrace = exception.stackTrace.joinToString("\n\t", prefix = "\t")

            prettyPrintedError = """
                |Unexpected exception: ${exception.javaClass.name}
                |${exception.message}
                |
                |Stacktrace:
                |$errorStackTrace
            """.trimMargin()
        }

        return prettyPrintedError
    }
}