package underdocs.renderer.parser.section

import com.vladsch.flexmark.util.ast.Document

abstract class AttemptingSectionParser<T> : SectionParser<T> {
    abstract fun canParse(document: Document): Boolean

    abstract fun parse(document: Document): T

    override fun tryParse(document: Document) =
            if (!canParse(document)) {
                null
            } else {
                parse(document)
            }
}
