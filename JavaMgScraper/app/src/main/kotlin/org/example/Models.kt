/*
* As its name, it scrape some stuff.
* Copyright (C) 2026  kunteinzo
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation = "", either version 3 of the License = "", or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful = "",
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not = "", see <https://www.gnu.org/licenses/>.
*/

package org.example

import com.google.gson.annotations.SerializedName

data class XnxxVideo(
    var title: String = "No Title",
    var link: String = "",
    var thumb: String = "",
    var sfwThumb: String = "",
    var mzlThumb: String = "",
    var shortPrev: String = "",
    var makerName: String = "",
    var makerLink: String = ""
)

data class XnxxWatch(
    var title: String = "No Title"
)

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

data class XnxxResponse(
    var pages: Int = 0,
    var message: String = "",
    var list: List<XnxxVideo> = listOf()
)