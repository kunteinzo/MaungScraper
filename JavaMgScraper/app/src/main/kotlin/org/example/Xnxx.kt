/*
* As its name, it scrape some stuff.
* Copyright (C) 2026  kunteinzo
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package org.example

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.coroutines.executeAsync
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Xnxx {
    companion object {
        private const val BASE_URL = "https://www.xnxx.com/"
    }

    suspend fun search(query: String) = coroutineScope {
        val xnxxResp = XnxxResponse()
        OkHttpClient.Builder().build().newCall(
            Request.Builder()
                .url("${BASE_URL}search/$query")
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.32 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.32")
                .build()
        ).execute().use { rp ->
            if (rp.isSuccessful) {
                val array = arrayListOf<XnxxVideo>()
                val root = Jsoup.parse(rp.body.string())
                    root.select(".thumb-block").forEach { block ->
                        val id = block.attr("data-id")
                        val xnxx = XnxxVideo()
                        block.getElementsByAttributeValueStarting("href", "/porn-maker/").forEach { maker ->
                            xnxx.makerName = maker.text().trim()
                            xnxx.makerLink = maker.attr("href")
                        }
                        block.getElementsByAttribute("title").forEach { video ->
                            xnxx.title = video.attr("title")
                            xnxx.link = video.attr("href")
                        }
                        block.getElementById("pic_$id")?.let {
                            xnxx.thumb = it.attr("data-src")
                            xnxx.sfwThumb = it.attr("data-sfwthumb")
                            xnxx.shortPrev = it.attr("data-pvv")
                            xnxx.mzlThumb = it.attr("data-mzl")
                        }
                        
                        array.add(xnxx)
                    }
                    root.selectFirst(".pagination")?.selectFirst(".last-page")?.attr("href")?.let {
                        xnxxResp.pages = it.substring(it.lastIndexOf("/")+1).toIntOrNull() ?: 0
                    }
                
                xnxxResp.list = array
            }
            return@use xnxxResp
        }
    }
    
    suspend fun xnxx() = coroutineScope {
        OkHttpClient.Builder().build()
            .newCall(
                Request.Builder()
                    .url("https://api.ipify.org")
                    .build()
            ).execute().use { root ->
                return@coroutineScope root.code
            }
    }
}