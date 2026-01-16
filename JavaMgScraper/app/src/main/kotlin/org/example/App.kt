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

import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import kotlin.system.exitProcess
import java.io.*


fun main() {
    println("hello")
    runBlocking {
        println(Xnxx().search("japan"))
        // println(Xnxx().xnxx())
    }
    exitProcess(0)
    var pages = 0
    val client = OkHttpClient.Builder().build()
    val request = Request.Builder()
        .url("https://www.xnxx.com/best")
        .build()
    client.newCall(request).execute().use { response ->
        println(response.code)
        if (response.isSuccessful) {
            val root = Jsoup.parse(response.body.string())
            root.select(".thumb-block").forEach { block ->
                val id = block.attr("data-id")
                println(id)
                block.getElementsByAttributeValueStarting("href", "/porn-maker").forEach { maker ->
                    println("Maker name: "+maker.text())
                    println("Maker url: "+maker.attr("href"))
                }
                block.getElementsByAttribute("title").forEach { video ->
                    println("Title: " + video.attr("title"))
                    println("Url: " + video.attr("href"))
                }
                block.getElementById("pic_$id")?.let {
                    println("Src: "+it.attr("data-src"))
                    println("SFWSrc: "+it.attr("data-sfwthumb"))
                    println("Pvv: "+it.attr("data-pvv"))
                    println("mzl: "+it.attr("data-mzl"))
                }
            }
            root.selectFirst(".pagination")?.selectFirst(".last-page")?.attr("href")?.let {
                pages = it.substring(it.lastIndexOf("/")+1).toIntOrNull() ?: 0
            }
            println("Total Pages: $pages")
        }
        exitProcess(0)
    }
}