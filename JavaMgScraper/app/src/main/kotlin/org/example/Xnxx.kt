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

class Xnxx {
    companion object {
        private const val BASE_URL = "https://www.xnxx.com/"
    }

    private val client = OkHttpClient.Builder().build()
    suspend fun search(query: String): HashMap<String, *> = coroutineScope {
        val list = hashMapOf(
            "code" to 200,
            "pages" to 0,
            "data" to arrayListOf<HashMap<String, String>>()
        )
        val rp = client.newCall(
            Request.Builder()
                .url("${BASE_URL}search/$query")
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.32 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.32")
                .build()
        ).execute()
            // TODO: fix here, can't use closable use extension function
            if (rp.isSuccessful) {
                list["code"] = rp.code
                Jsoup.parse(rp.body.string()).let { root ->
                    root.select(".thumb-block").forEach { block ->
                        val id = block.attr("data-id")
                        val b = hashMapOf(
                            "mk_name" to "Empty",
                            "mk_link" to "Empty",
                            "title" to "No Title",
                            "link" to "",
                            "thumb" to "",
                            "sfw_thumb" to "",
                            "short_prev" to "",
                            "thumb_list" to ""
                        )
                        block.getElementsByAttributeValueStarting("href", "/porn-maker/").forEach { maker ->
                            b["mk_name"] = maker.text().trim()
                            b["mk_link"] = maker.attr("href")
                        }
                        block.getElementsByAttribute("title").forEach { video ->
                            b["title"] = video.attr("title")
                            b["link"] = video.attr("href")
                        }
                        block.getElementById("pic_$id")?.let {
                            b["thumb"] = it.attr("data-src")
                            b["sfw_thumb"] = it.attr("data-sfwthumb")
                            b["short_prev"] = it.attr("data-pvv")
                            b["thumb_list"] = it.attr("data-mzl")
                        }
                        
                        (list["data"] as ArrayList<HashMap<String, String>>).add(b)
                    }
                    root.selectFirst(".pagination")?.selectFirst(".last-page")?.attr("href")?.let {
                        list["pages"] = it.substring(it.lastIndexOf("/")+1).toIntOrNull() ?: 0
                    }
                    println("Total Pages: ${list["pages"]}")
                }
            } else {
                list["code"] = rp.code
            }
        return@coroutineScope list
    }
}