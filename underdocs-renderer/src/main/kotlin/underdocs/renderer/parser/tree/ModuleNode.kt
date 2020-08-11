package underdocs.renderer.parser.tree

import underdocs.representation.Header

data class ModuleNode(
    val path: String,
    val children: Map<String, ModuleNode>,
    val headers: List<Header>
)
