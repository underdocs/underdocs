package underdocs.parser.comment.singleline

import org.eclipse.cdt.core.dom.ast.IASTComment
import underdocs.configuration.domain.CommentStyle
import underdocs.parser.comment.CommentGroupProcessor
import java.lang.StringBuilder

class DoubleSlashCommentGroupProcessor: CommentGroupProcessor {
    private val PREFIX_REGEX = Regex("^\\s*//[^/]")

    override fun processedCommentStyle() = CommentStyle.SINGLE_LINE_DOUBLE_SLASH

    override fun canProcessNextGroup(nodes: List<IASTComment>): Boolean {
        val firstNode = nodes.first()

        if (firstNode.isBlockComment) {
            return false
        }

        val firstComment = String(firstNode.comment)

        return PREFIX_REGEX.containsMatchIn(firstComment)
    }

    override fun processNextGroup(nodes: MutableIterator<IASTComment>): String {
        var currentNode = nodes.next()
        val firstComment = String(currentNode.comment)

        val prefixAndWhitespaceTrimmed = firstComment.trimStart('/').trimStart()

        val prefixAndWhitespaceLength = firstComment.length - prefixAndWhitespaceTrimmed.length

        val accumulator = StringBuilder(prefixAndWhitespaceTrimmed)

        var previousNode = currentNode

        nodes.remove()

        while (nodes.hasNext()) {
            currentNode = nodes.next()

            if ((currentNode.fileLocation.startingLineNumber - previousNode.fileLocation.startingLineNumber) > 1) {
                break
            }

            val comment = String(currentNode.comment)

            if (!PREFIX_REGEX.containsMatchIn(comment)) {
                break
            }

            accumulator.append('\n')
            accumulator.append(comment.substring(prefixAndWhitespaceLength))

            previousNode = currentNode
            nodes.remove()
        }

        return accumulator.toString()
    }
}