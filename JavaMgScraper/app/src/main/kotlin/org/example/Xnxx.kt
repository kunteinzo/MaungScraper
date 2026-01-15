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
    suspend fun search(query: String): String = coroutineScope {
        client.newCall(
            Request.Builder()
                .url("${BASE_URL}search/$query")
                .build()
        ).execute().use { rp ->
            // TODO: fix here
            if (rp.isSuccessful) {
                Jsoup.parse(rp.body.string()).use { root ->
                    var pages = 0
                    root.select(".thumb-block").forEach { block ->
                        val id = block["data-id"]
                        block.getElementsByAttributeValueStarting("href", "/porn-maker/").forEach { maker ->
                            println("Maker name: "+maker.text().trim())
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
                    return@coroutineScope "${rp.code}"
                }
            }
        }
        return@coroutineScope "Empty"
    }
}