package mg.scrape.models

import com.google.gson.annotations.SerializedName

data class XnxxRelated(
    @SerializedName("t")
    var title: String?,
    @SerializedName("pn")
    var makerName: String?,
    @SerializedName("pu")
    var makerLink: String?,
    @SerializedName("d")
    var duration: String?,
    @SerializedName("r")
    var rating: String?,
    @SerializedName("u")
    var link: String?,
    @SerializedName("i")
    var thumb: String?,
    @SerializedName("il")
    var thumbL: String?,
    @SerializedName("ip")
    var thumbLL: String?,
    @SerializedName("mu")
    var thumbMzl: String?,
    @SerializedName("ipu")
    var shortPrev: String?
)