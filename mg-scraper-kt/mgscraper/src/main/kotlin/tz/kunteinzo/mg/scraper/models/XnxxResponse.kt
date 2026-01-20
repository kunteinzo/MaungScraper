package tz.kunteinzo.mg.scraper.models

data class XnxxResponse(
    var pages: Int = 0,
    var message: String = "",
    var list: List<XnxxVideo> = listOf()
)