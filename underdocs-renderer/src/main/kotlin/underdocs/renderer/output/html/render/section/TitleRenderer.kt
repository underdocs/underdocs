package underdocs.renderer.output.html.render.section

import j2html.TagCreator.h1
import j2html.tags.Tag

class TitleRenderer {
    fun render(name: String, attributes: Map<String, String>): Tag<*> {
        return h1(name);
    }
}