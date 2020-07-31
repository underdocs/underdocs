package underdocs.renderer.output.html.render.section

import com.vladsch.flexmark.ext.gitlab.GitLabExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import j2html.TagCreator.a
import j2html.TagCreator.code
import j2html.TagCreator.div
import j2html.TagCreator.each
import j2html.TagCreator.h1
import j2html.TagCreator.h2
import j2html.TagCreator.h3
import j2html.TagCreator.h4
import j2html.TagCreator.iff
import j2html.TagCreator.img
import j2html.TagCreator.pre
import j2html.TagCreator.section
import j2html.TagCreator.span
import j2html.TagCreator.table
import j2html.TagCreator.tbody
import j2html.TagCreator.td
import j2html.TagCreator.tr
import j2html.tags.ContainerTag
import j2html.tags.DomContent
import j2html.tags.Tag
import j2html.tags.Text
import j2html.tags.UnescapedText
import underdocs.renderer.output.html.link.Linker
import underdocs.renderer.output.html.render.misc.iffPrimitive
import underdocs.renderer.representation.EnumElement
import underdocs.renderer.representation.EnumMember
import underdocs.renderer.representation.EnumType
import underdocs.renderer.representation.Function
import underdocs.renderer.representation.MacroFunction
import underdocs.renderer.representation.Module
import underdocs.renderer.representation.ReferredType
import underdocs.renderer.representation.Struct
import underdocs.renderer.representation.StructMember
import underdocs.renderer.representation.StructType
import underdocs.renderer.representation.TopLevelElement
import underdocs.renderer.representation.Union
import underdocs.renderer.representation.UnionMember
import underdocs.renderer.representation.UnionType
import underdocs.renderer.representation.VariableMember
import underdocs.renderer.representation.Visitable
import underdocs.renderer.representation.documentation.ErrorHandling
import underdocs.renderer.representation.documentation.Example
import underdocs.renderer.representation.documentation.ReturnValue
import underdocs.renderer.representation.documentation.ReturnValueItem
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class SectionRenderer(private val linker: Linker) {
    private val KNOWN_ATTRIBUTE_LIST_ELEMENTS = listOf<String>("since", "stability")

    private val representationRenderer = RepresentationRenderer()
    private val singleLineTypeRenderer = SingleLineTypeRenderer()

    fun renderHeading(visitable: Visitable, title: String, attributes: Map<String, String>): Tag<*> {
        val titleElements = ArrayList<DomContent>()

        titleElements.add(h1(title))

        linker.remoteLinkToVisitable(visitable)?.let { remoteLink ->
            val link = a(
                    img().withSrc(linker.siteLinkBetween(visitable, "_static/file-link-icon.svg"))
            ).withHref(remoteLink)

            titleElements.add(div(link).withClass("content-heading-remote-link"))
        }

        val headingElements = ArrayList<DomContent>()

        headingElements.add(div().with(titleElements).withClass("content-heading-title"))

        renderAttributeList(visitable, attributes)?.let { attributeListTag ->
            headingElements.add(attributeListTag)
        }

        return div().with(headingElements).withClass("content-heading")
    }

    private fun renderAttributeList(visitable: Visitable, attributes: Map<String, String>): Tag<*>? {
        if (KNOWN_ATTRIBUTE_LIST_ELEMENTS.none { attributes.containsKey(it) }) {
            return null
        }

        val attributeElements = attributes.map { (key, value) ->
            when (key) {
                "since" -> linker.remoteLinkToTag(value)?.let { tagLink ->
                    span(
                            span(key).withClass("attribute-key"),
                            span(":").withClass("attribute-separator"),
                            span(a(value).withHref(tagLink).withClass("since-attribute-link")).withClass("attribute-value")
                    )
                } ?: renderSimpleAttribute(key, value)
                else -> renderSimpleAttribute(key, value)
            }
        }

        return div().with(attributeElements).withClass("attribute-list")
    }

    private fun renderSimpleAttribute(key: String, value: String) = span(
            span(key).withClass("attribute-key"),
            span(":").withClass("attribute-separator"),
            span(value).withClass("attribute-value")
    )

    fun renderMarkdown(mdText: String): UnescapedText {
        val options = MutableDataSet()

        options.set(Parser.EXTENSIONS, listOf(TablesExtension.create(), YamlFrontMatterExtension.create(), GitLabExtension.create()))
        options.set(GitLabExtension.INLINE_MATH_CLASS, "katex-inline")
        options.set(GitLabExtension.BLOCK_MATH_CLASS, "katex-block")
        val parser = Parser.builder(options).build()

        val renderer = HtmlRenderer.builder(options).build()

        val node = parser.parse(mdText)
        val htmlText = renderer.render(node)

        return UnescapedText(htmlText)
    }

    fun renderRepresentation(element: TopLevelElement) = section(
            representationRenderer.render(element)
    ).withClass("representation")

    fun renderExcerpt(excerpt: String): Tag<*> {
        TODO()
    }

    fun renderMembers(element: TopLevelElement) = when (element) {
        is EnumElement -> section(
                h2("Members"),
                each(element.members) { member ->
                    div(
                            div(
                                    h3(member.name).withClass("member-title"),
                                    iff(Optional.ofNullable(member.value)) { value ->
                                        span("(value = $value)").withClass("member-type")
                                    }
                            ).withClass("member-heading"),
                            div(
                                    iff(Optional.ofNullable(member.documentation?.description)) {
                                        renderMarkdown(it)
                                    }
                            ).withClass("member-description")
                    ).withClass("member")
                }
        ).withClass("members")
        is Struct -> section(
                h2("Members"),
                each(element.members) { renderMember(it) }
        ).withClass("members")
        is Union -> section(
                h2("Members"),
                each(element.members) { renderMember(it) }
        ).withClass("members")
        else -> div()
    }

    private fun renderMember(member: underdocs.renderer.representation.Member): Tag<*> {
        val name = when (member) {
            is EnumMember -> if (member.name != null && member.name.isNotEmpty()) {
                member.name
            } else {
                "anonymous enum"
            }
            is StructMember -> if (member.name != null && member.name.isNotEmpty()) {
                member.name
            } else {
                "anonymous struct"
            }
            is UnionMember -> if (member.name != null && member.name.isNotEmpty()) {
                member.name
            } else {
                "anonymous union"
            }
            is VariableMember -> member.name
            else -> "unknown"
        }

        val type = when (member) {
            is EnumMember -> "enum declaration"
            is StructMember -> "struct declaration"
            is UnionMember -> "union declaration"
            is VariableMember -> when (member.type) {
                is EnumType -> if (member.type.name != null && member.type.name.isNotEmpty()) {
                    member.type.name
                } else {
                    "inline enum"
                }
                is ReferredType -> {
                    var specifiers = member.type.specifiers.joinToString(" ")

                    if (specifiers.isNotEmpty()) {
                        specifiers += ""
                    }

                    "$specifiers${member.type.name}"
                }
                is StructType -> if (member.type.name != null && member.type.name.isNotEmpty()) {
                    member.type.name
                } else {
                    "inline struct"
                }
                is UnionType -> if (member.type.name != null && member.type.name.isNotEmpty()) {
                    member.type.name
                } else {
                    "inline union"
                }
                else -> "unknown"
            }
            else -> "unknown"
        }

        val description = when (member) {
            is EnumMember -> member.documentation?.description
            is StructMember -> member.documentation?.description
            is UnionMember -> member.documentation?.description
            is VariableMember -> member.documentation?.description
            else -> null
        }

        return div(
                div(
                        h3(name).withClass("member-title"),
                        span(type).withClass("member-type")
                ).withClass("member-heading"),
                div(
                        iff(Optional.ofNullable(description)) {
                            renderMarkdown(it)
                        }
                ).withClass("member-description")
        ).withClass("member")
    }

    fun renderDescription(description: String) = div(
            renderMarkdown(description)
    ).withClass("description")

    fun renderParameters(element: TopLevelElement) = when (element) {
        is MacroFunction -> section(
                h2("Parameters"),
                each(element.documentation!!.parameters) { (name, description) ->
                    div(
                            div(
                                    h3(name).withClass("parameter-title")
                            ).withClass("parameter-heading"),
                            div(renderMarkdown(description)).withClass("parameter-description")
                    ).withClass("parameter")
                }
        ).withClass("parameters")
        is Function -> section(
                h2("Parameters"),
                each(element.parameters) { parameter ->
                    val description = element.documentation?.parameters?.get(parameter.name)

                    div(
                            div(
                                    h3(parameter.name).withClass("parameter-title"),
                                    span(singleLineTypeRenderer.render(parameter.type)).withClass("parameter-type")
                            ).withClass("parameter-heading"),
                            iff(Optional.ofNullable(description)) {
                                div(renderMarkdown(it)).withClass("parameter-description")
                            }
                    ).withClass("parameter")
                }
        ).withClass("parameters")
        else -> div()
    }

    fun renderExamples(examples: List<Example>) = section(
            h2("Examples"),
            each(examples) { example ->
                val elements = ArrayList<DomContent>()

                elements.add(h3(example.title).withClass("example-title"))

                example.description?.let {
                    elements.add(div(renderMarkdown(it)).withClass("example-description"))
                }

                elements.add(renderExampleCode(example.code))

                example.output?.let {
                    elements.add(div(
                            h4("Possible Output"),
                            div(
                                    pre(
                                            code(it).withClass("language-output")
                                    ).withClass("line-numbers")
                            ).withClass("example-output-code")
                    ).withClass("example-output"))
                }

                div().with(elements).withClass("example")
            }
    ).withClass("examples")

    private fun renderExampleCode(source: String) = div(
            pre(
                    code(source).withClass("language-c")
            ).withClass("line-numbers")
    ).withClass("example-code")

    fun renderSeeAlso(elements: List<String>) = section(
            h2("See Also")
    ).withClass("see-also")

    fun renderReturnValue(function: Function, returnValue: ReturnValue) = section(
        div(
            h2("Return Value"),
            span(
                singleLineTypeRenderer.render(function.returnType)
            ).withClass("return-value-type")
        ).withClass("return-value-heading"), div(
            h3("Success"), renderReturnValueItemsTable(returnValue.success)
        ),
        iffPrimitive(returnValue.error.isNotEmpty()) {
            div(
                h3("Error"), renderReturnValueItemsTable(returnValue.error)
            )
        }
    ).withClass("return-value")

    private fun renderReturnValueItemsTable(returnValueItemList: List<ReturnValueItem>): ContainerTag? {
        return table(
            tbody(
                each(returnValueItemList) { child ->
                    tr(
                        td(
                            child.value
                        ).withClass("return-value-name-cell"),
                        td(
                            div(
                                renderMarkdown(child.description)
                            )
                        ).withClass("return-value-excerpt-cell")
                    )
                }
            )
        ).withClass("return-value-table")
    }

    fun renderErrorHandling(errorHandling: ErrorHandling): ContainerTag = section(
        h2("Error Handling"),
            each(errorHandling.errorHandlingItems) { child ->
                div(
                    div(
                        renderMarkdown(child.actionState)
                    ).withClass("error-handling-action-state"),
                    div(
                        renderMarkdown(child.condition)
                    ).withClass("error-handling-excerpt")
                )
            }
    ).withClass("error-handling")

    fun renderNotes(notes: String) = section(
            h2("Notes"),
            renderMarkdown(notes)
    ).withClass("notes")

    fun renderOtherSections(otherSections: Map<String, String>) = div(
            each(otherSections) { (title, content) ->
                section(
                        h2(title),
                        renderMarkdown(content)
                ).withClass("other-section")
            }
    ).withClass("other-sections")

    fun renderSubmodules(parent: Module): Tag<*> {
        val orderedChildren = parent.children.values.sortedBy { titleForModule(it) }

        return section(
                h2("Submodules"),
                table(
                        tbody(
                                each(orderedChildren) { child ->
                                    val linkText = titleForModule(child)

                                    tr(
                                            td(
                                                    a(linkText).withHref(linker.siteLinkBetween(parent, child))
                                            ).withClass("submodule-name-cell"),
                                            td(
                                                    child.documentation?.excerpt?.let {
                                                        renderMarkdown(it)
                                                    } ?: Text("")
                                            ).withClass("submodule-excerpt-cell")
                                    )
                                }
                        )
                ).withClass("submodules-table")
        ).withClass("submodules")
    }

    private fun titleForModule(module: Module) = module.documentation?.title
            ?: module.path.split(File.separator).last()

    fun renderSubheaders(module: Module): Tag<*> {
        return section(
                h2("Headers"),
                table(
                        tbody(
                                each(module.headers) {
                                    tr(
                                            td(
                                                    a(it.filename).withHref(linker.siteLinkBetween(module, it))
                                            ).withClass("subheader-name-cell"),
                                            td(
                                                    it.documentation?.excerpt?.let {
                                                        renderMarkdown(it)
                                                    } ?: Text("")
                                            ).withClass("subheader-excerpt-cell")
                                    )
                                }
                        )
                ).withClass("subheaders-table")
        ).withClass("subheaders")
    }
}