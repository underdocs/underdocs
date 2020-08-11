package underdocs.error

import java.lang.RuntimeException

abstract class BaseUnderdocsError : RuntimeException {
    val code: String
    val title: String
    private val _context: MutableMap<String, String>
    val context: Map<String, String>
        get() = _context

    init {
        _context = HashMap()
    }

    constructor(code: String, title: String) {
        this.code = code
        this.title = title
    }

    constructor(code: String, title: String, cause: Throwable) : super(cause) {
        this.code = code
        this.title = title
    }

    abstract fun getDescription(): String

    abstract fun getSolution(): String

    fun addToContext(key: String, value: String): BaseUnderdocsError {
        _context[key] = value

        return this
    }
}
