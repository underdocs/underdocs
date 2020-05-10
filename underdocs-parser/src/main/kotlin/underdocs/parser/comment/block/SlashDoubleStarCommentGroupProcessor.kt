package underdocs.parser.comment.block

import org.eclipse.cdt.core.dom.ast.IASTComment
import underdocs.configuration.domain.CommentStyle
import underdocs.parser.comment.CommentGroupProcessor

class SlashDoubleStarCommentGroupProcessor : CommentGroupProcessor {
    private val START_REGEX = Regex("^\\s*/\\*\\*\\s*$")
    private val END_REGEX = Regex("^\\s*\\*/\\s*$")

    override fun processedCommentStyle() = CommentStyle.BLOCK_SLASH_DOUBLE_STAR

    override fun canProcessNextGroup(nodes: List<IASTComment>): Boolean {
        val node = nodes.first()

        if (!node.isBlockComment) {
            return false
        }

        val comment = String(node.comment)

        val lines = comment.split('\n')

        if (lines.size < 3) {
            return false
        }

        if (!START_REGEX.matches(lines.first())) {
            return false
        }

        if (!END_REGEX.matches(lines.last())) {
            return false
        }

        return true
    }

    override fun processNextGroup(nodes: MutableIterator<IASTComment>): String {
        val node = nodes.next();
        nodes.remove()

        val comment = String(node.comment)

        val textLines = comment.split('\n')
                .drop(1)
                .dropLast(1)

        val firstLine = textLines.first()

        val firstLineTrimmed = firstLine.trimStart().drop(1).trimStart()

        val prefixLength = firstLine.length - firstLineTrimmed.length

        val accumulator = StringBuilder(firstLineTrimmed)

        for (line in textLines.drop(1)) {
            accumulator.append(line.substring(prefixLength))
            accumulator.append('\n')
        }

        return accumulator.toString().trimEnd()
    }
}