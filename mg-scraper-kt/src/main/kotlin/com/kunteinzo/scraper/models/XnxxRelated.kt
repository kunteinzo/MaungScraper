package com.kunteinzo.scraper.models

// TODO: finish this
data class XnxxRelated(
    var id: Long,
    @SerializedName("t")
    var title: String,
    @SerializedName("u")
    var url: String,
    @SerializedName("d")
    var duration: String,
    @SerializedName("r")
    var rating: String,
    @SerializedName("pn")
    var makerName: String,
    @SerializedName("pu")
    var makerLink: String,
)
