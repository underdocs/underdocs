package underdocs.renderer.parser.section

import com.vladsch.flexmark.ast.CodeBlock
import com.vladsch.flexmark.ast.FencedCodeBlock
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.IndentedCodeBlock
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import underdocs.renderer.representation.documentation.Example

class ExamplesParser : underdocs.renderer.parser.section.AttemptingSectionParser<List<Example>>() {
    override fun canParse(document: Document) =
        document.children.any { isSectionHeadingWithTitle(it, "Examples") }

    override fun parse(document: Document): List<Example> {
        val result = mutableListOf<Example>()

        val startNode = document.children.first { isSectionHeadingWithTitle(it, "Examples") }

        var currentExampleHeading = nextNodeInSectionWhere(startNode) { isExampleHeading(it) } as Heading?

        val sectionEndNode = if (sectionEndNodeFrom(startNode) == document.lastChild) {
            document.lastChild
        } else {
            sectionEndNodeFrom(startNode).previous
        }

        while (currentExampleHeading != null) {
            val nextExampleHeading = nextNodeInSectionWhere(currentExampleHeading) {
                isExampleHeading(it)
            } as Heading?

            var lastCodeBlock = sectionEndNode

            while (isNotCode(lastCodeBlock) && lastCodeBlock != currentExampleHeading) {
                lastCodeBlock = lastCodeBlock.previous
            }

            if (isNotCode(lastCodeBlock)) {
                continue
            }

            val secondLastCodeBlock = lastCodeBlock.previous

            if (isNotCode(secondLastCodeBlock)) {
                result.add(parseWithExampleOnly(currentExampleHeading, lastCodeBlock))
            } else {
                result.add(parseWithExampleAndOutput(currentExampleHeading, secondLastCodeBlock, lastCodeBlock))
            }

            currentExampleHeading = nextExampleHeading
        }

        return result
    }

    private fun isNotCode(node: Node) =
        node !is CodeBlock && node !is FencedCodeBlock && node !is IndentedCodeBlock

    private fun parseWithExampleOnly(heading: Heading, exampleBlock: Node): Example {
        val explanation = extractTextBetweenNodes(heading.next, exampleBlock)

        val code = exampleBlock.firstChild.chars.toString()
        val language = if (exampleBlock is FencedCodeBlock) {
            exampleBlock.info.toString()
        } else {
            null
        }

        return Example(
            heading.text.toString(),
            explanation,
            code,
            language,
            null
        )
    }

    private fun parseWithExampleAndOutput(heading: Heading, exampleBlock: Node, outputBlock: Node): Example {
        val explanation = extractTextBetweenNodes(heading.next, exampleBlock.previous)

        val code = exampleBlock.firstChild.chars.toString()
        val language = if (exampleBlock is FencedCodeBlock) {
            exampleBlock.info.toString()
        } else {
            null
        }

        val output = outputBlock.firstChild.chars.toString()

        return Example(
            heading.text.toString(),
            explanation,
            code,
            language,
            output
        )
    }

    private fun isExampleHeading(node: Node) =
        node is Heading && node.level == 3
}
