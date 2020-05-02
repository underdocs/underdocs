package underdocs.configuration

import underdocs.configuration.domain.CommentStyle

data class ParserConfiguration(
        val documentationCommentStyles: Set<CommentStyle>
)
