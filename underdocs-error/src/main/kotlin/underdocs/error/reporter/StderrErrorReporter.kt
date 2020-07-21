package underdocs.error.reporter

import underdocs.error.environment.EnvironmentDetailCollector
import underdocs.error.pretty.ErrorPrettyPrinter

class StderrErrorReporter : ErrorReporter {
    private val environmentDetailCollector: EnvironmentDetailCollector
    private val errorPrinter: ErrorPrettyPrinter

    override fun report(exception: Exception) {
        val prettyError = errorPrinter.print(exception)
        val environmentDetails = environmentDetailCollector.collect()

        val output = """
            $prettyError
            
            $environmentDetails
        """.trimIndent()

        System.err.println(output)
    }
}