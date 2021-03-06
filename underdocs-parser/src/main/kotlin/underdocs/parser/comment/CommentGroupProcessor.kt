package underdocs.parser.comment

import org.eclipse.cdt.core.dom.ast.IASTComment
import underdocs.configuration.domain.CommentStyle

interface CommentGroupProcessor {
    fun processedCommentStyle(): CommentStyle

    fun canProcessNextGroup(nodes: List<IASTComment>): Boolean

    fun processNextGroup(nodes: MutableIterator<IASTComment>): String
}
