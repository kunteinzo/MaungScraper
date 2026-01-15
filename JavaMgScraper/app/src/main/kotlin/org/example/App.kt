package org.example

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import kotlin.system.exitProcess


fun main() {
    println("hello")
    /*var pages = 0
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
    }*/

    runBlocking {
        println("1. "+Xnxx().search(""))
        println("2. "+Xnxx().search(""))
    }
}
