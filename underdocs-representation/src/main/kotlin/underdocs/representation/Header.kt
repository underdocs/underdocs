package underdocs.representation

data class Header(
        val path: String,
        val filename: String,

        val elements: List<Element>,

        val comment: String?
)
