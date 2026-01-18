package mg.scrape.models

data class XnxxBest(
    var months: List<String> = listOf(),
    var totalPages: Int = 0,
    var list: List<XnxxVideo> = listOf()
)
