package underdocs.renderer.parser.section

import com.vladsch.flexmark.util.ast.Document

interface SectionParser<T> {
    fun tryParse(document: Document): T?
}
