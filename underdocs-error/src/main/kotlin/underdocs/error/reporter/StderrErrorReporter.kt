package underdocs.error.reporter

import underdocs.error.environment.DefaultEnvironmentDetailCollector
import underdocs.error.environment.EnvironmentDetailCollector
import underdocs.error.pretty.DefaultErrorPrettyPrinter
import underdocs.error.pretty.ErrorPrettyPrinter
import underdocs.version.retriever.VersionRetriever

class StderrErrorReporter : ErrorReporter {
    private val environmentDetailCollector: EnvironmentDetailCollector
    private val errorPrinter: ErrorPrettyPrinter

    init {
        environmentDetailCollector = DefaultEnvironmentDetailCollector(VersionRetriever.create())
        errorPrinter = DefaultErrorPrettyPrinter()
    }

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