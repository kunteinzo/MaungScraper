package tz.kunteinzo.mg.scraper.models

data class XnxxWatch(
    var title: String = "No Title",
    var description: String = "",
    var makerName: String = "",
    var makerLink: String = "",
    var hls: String = "",
    var thumb: String = "",
    var thumb169: String = "",
    var slide: String = "",
    var slideBig: String = "",
    var slideMinute: String = "",
    var mzl: String = "",
    var pvv: String = "",
    var related: List<XnxxRelated> = listOf()
)