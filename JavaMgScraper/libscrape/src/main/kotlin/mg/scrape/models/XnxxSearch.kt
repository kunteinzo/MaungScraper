package mg.scraper.models

data class XnxxSearch(
    var totalPages: Int = 0,
    var list: List<XnxxVideo> = listOf()
)