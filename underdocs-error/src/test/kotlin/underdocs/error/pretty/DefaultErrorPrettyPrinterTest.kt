package underdocs.error.pretty

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import underdocs.error.BaseUnderdocsError

class DefaultErrorPrettyPrinterTest {

    companion object {
        const val TITLE = "Test Exception"
        const val ERROR_CODE = "42"
        const val DESCRIPTION = "This is a dummy exception for tests."
        const val SOLUTION = "There is no solution for this."
        const val RUNTIME_EXCEPTION_CLASSPATH = "java.lang.RuntimeException"
        const val RUNTIME_EXCEPTION_DESCRIPTION = "RuntimeException Description"
    }

    private val defaultErrorPrettyPrinter = DefaultErrorPrettyPrinter()

    @Test
    fun print_WhenBaseUnderDocsErrorTypeExceptionIsThrown_ShouldReturnMessageContainingTheDetailsOfTheException() {
        // Given
        val expectedBaseUnderDocsErrorTypeMessage =
                """
                |${TITLE.toUpperCase()} - Error Code $ERROR_CODE
                |Details
                |${DESCRIPTION}
                |
                |Possible solution
                |${SOLUTION}
            """.trimMargin()

        // When
        val message = defaultErrorPrettyPrinter.print(TestException(ERROR_CODE, TITLE))

        // Then
        assertEquals(expectedBaseUnderDocsErrorTypeMessage, message)
    }

    @Test
    fun print_WhenUnexpectedExceptionIsThrown_ShouldReturnMessageContainingTheDetailsOfTheException() {
        // Given
        val expectedUnexpectedExceptionMessage =
                """
                |Unexpected exception $RUNTIME_EXCEPTION_CLASSPATH
                |$RUNTIME_EXCEPTION_DESCRIPTION
                |
                |Stacktrace
            """.trimMargin()

        // When
        val message = defaultErrorPrettyPrinter.print(RuntimeException(RUNTIME_EXCEPTION_DESCRIPTION))

        // Then
        assertTrue(message.startsWith(expectedUnexpectedExceptionMessage))
    }

    class TestException(code: String, title: String) : BaseUnderdocsError(code, title) {
        override fun getDescription(): String = DESCRIPTION
        override fun getSolution(): String = SOLUTION
    }

}