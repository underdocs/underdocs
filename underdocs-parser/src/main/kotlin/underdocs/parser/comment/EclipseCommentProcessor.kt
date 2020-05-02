package underdocs.parser.comment

import org.eclipse.cdt.core.dom.ast.IASTComment
import underdocs.parser.comment.block.SlashDoubleStarCommentGroupProcessor
import underdocs.parser.comment.block.SlashStarCommentGroupProcessor
import underdocs.parser.comment.singleline.DoubleSlashCommentGroupProcessor
import underdocs.parser.comment.singleline.TripleSlashCommentGroupProcessor
import java.util.LinkedList

class EclipseCommentProcessor(private val documentationCommentStyles: Set<CommentStyle>,
                              private val mergeTrailingComments: Boolean) {
    private val processors = listOf(
            TripleSlashCommentGroupProcessor(),
            DoubleSlashCommentGroupProcessor(),
            SlashDoubleStarCommentGroupProcessor(),
            SlashStarCommentGroupProcessor()
    )

    fun extractCommentFromNodes(leadingCommentNodes: List<IASTComment>, trailingCommentNodes: List<IASTComment>): String? {
        val nodes = when {
            mergeTrailingComments -> LinkedList(leadingCommentNodes.plus(trailingCommentNodes))
            else -> LinkedList(leadingCommentNodes)
        }

        if (nodes.isEmpty()) {
            return null
        }

        val accumulator = StringBuilder()

        while (nodes.isNotEmpty()) {
            val processor = processors.firstOrNull { it.canProcessNextGroup(nodes) } ?: break

            if (documentationCommentStyles.contains(processor.processedCommentStyle())) {
                accumulator.append(processor.processNextGroup(nodes.iterator()))
                accumulator.append('\n')
            } else {
                // Simply process and discard
                processor.processNextGroup(nodes.iterator())
            }
        }

        val documentation = accumulator.toString().trimEnd();

        return if (documentation.isBlank()) {
            null
        } else {
            documentation
        }
    }
}
