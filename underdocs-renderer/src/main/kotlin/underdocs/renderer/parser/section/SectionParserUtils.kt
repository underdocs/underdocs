package underdocs.renderer.parser.section

import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.util.ast.Node

fun extractTextBetweenNodes(firstNode: Node, lastNode: Node) =
        firstNode.document.chars.substring(firstNode.startOffset, lastNode.endOffset);

fun nextNodeWhere(startNode: Node, predicate: (Node) -> Boolean): Node? {
    var currentNode: Node? = startNode.next

    while (currentNode != null) {
        if (predicate(currentNode)) {
            return currentNode
        }

        currentNode = currentNode.next
    }

    return null
}

fun nextNodeInSectionWhere(startNode: Node, predicate: (Node) -> Boolean): Node? {
    var currentNode: Node? = startNode.next

    while (currentNode != null) {
        if (isSectionHeading(currentNode)) {
            return null
        }

        if (predicate(currentNode)) {
            return currentNode
        }

        currentNode = currentNode.next
    }

    return null
}

fun sectionEndNodeFrom(nodeInSection: Node) =
        nextNodeWhere(nodeInSection) { isSectionHeading(it) }
                ?.previous
                ?: nodeInSection.document.lastChild

fun lastNodeBefore(startNode: Node, predicate: (Node) -> Boolean): Node? {
    var currentNode: Node? = startNode.next?.next

    while (currentNode != null) {
        if (predicate(currentNode)) {
            return currentNode.previous
        }

        currentNode = currentNode.next
    }

    return null
}

fun lastNodeBeforeOrLast(startNode: Node, predicate: (Node) -> Boolean): Node =
        lastNodeBefore(startNode, predicate) ?: startNode.document.lastChild

fun isSectionHeading(node: Node) =
        node is Heading && node.level == 2

fun isSectionHeadingWithTitle(node: Node, title: String) =
        isSectionHeading(node) && (node as Heading).text.toString() == title
