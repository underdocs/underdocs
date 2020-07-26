package underdocs.renderer.output.html.render.misc

fun <T> iffPrimitive(condition: Boolean, ifFunction: () -> T?): T? {
    return if (condition) ifFunction.invoke() else null
}
