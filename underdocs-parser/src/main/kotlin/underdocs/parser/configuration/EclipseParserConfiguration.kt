package underdocs.parser.configuration

import underdocs.parser.comment.CommentStyle

data class EclipseParserConfiguration(
    val documentationCommentStyles: Set<CommentStyle>
)
