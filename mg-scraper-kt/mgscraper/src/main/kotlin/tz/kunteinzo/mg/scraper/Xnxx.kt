package tz.kunteinzo.mg.scraper

import tz.kunteinzo.mg.scraper.models.XnxxResponse
import tz.kunteinzo.mg.scraper.models.XnxxVideo
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class Xnxx {
    companion object {
        private const val BASE_URL = "https://www.xnxx.com"
    }

    fun req(url: String) = OkHttpClient.Builder().build().newCall(Request.Builder()
        .url(url)
        .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.32 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.32")
        .build())

    suspend fun search(query: String) = coroutineScope {
        val xnxxResp = XnxxResponse()
        try {
            req("${BASE_URL}/search/$query").execute().use { resp ->
                if (resp.isSuccessful) {
                    val array = arrayListOf<XnxxVideo>()
                    val root = Jsoup.parse(resp.body.string())
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
                        xnxxResp.pages = it.substring(it.lastIndexOf("/") + 1).toIntOrNull() ?: 0
                    }

                    xnxxResp.list = array
                }
                xnxxResp.message = "Fetch Success"
                return@use xnxxResp
            }
        } catch (e: Exception) {
            xnxxResp.message = e.message.toString()
            return@coroutineScope xnxxResp
        }
    }

    suspend fun watch(link: String) = coroutineScope {
        try {
            req("$BASE_URL$link").execute().use { resp ->
                    val root = Jsoup.parse(resp.body.string())
                    root.getElementsByTag("script").find { it.data().contains("VideoHLS") }?.data()?.let { player ->
                        "VideoHLS.*'".toRegex().find(player)?.value?.let { hls ->
                            println(hls.substring(10, hls.length - 1))
                        }
                        "ThumbUrl.*'".toRegex().find(player)?.value?.let { thumbUrl ->
                            println(thumbUrl.substring(10, thumbUrl.length - 1))
                        }
                        "ThumbUrl169.*'".toRegex().find(player)?.value?.let { thumbUrl169 ->
                            println(thumbUrl169.substring(13, thumbUrl169.length - 1))
                        }
                        "ThumbSlideMinute.*'".toRegex().find(player)?.value?.let { slideMinute ->
                            println(slideMinute.substring(18, slideMinute.length - 1))
                        }
                        "ThumbSlideBig.*'".toRegex().find(player)?.value?.let { slideBig ->
                            println(slideBig.substring(15, slideBig.length - 1))
                        }
                        "ThumbSlide.*'".toRegex().find(player)?.value?.let { slide ->
                            println(slide.substring(12, slide.length - 1))
                        }
                    }
                    println(root.selectFirst(".video-title")?.text()?.trim())
                    println(root.selectFirst(".video-description")?.text()?.trim())
                    root.selectFirst(".free-plate")?.let { maker ->
                        println(maker.text().trim())
                        println(maker.attr("href"))
                    }
                    root.getElementsByTag("script").find { it.data().contains("video_related=") }?.data()
                        ?.let { related ->
                            "=.*];".toRegex().find(related)?.value?.let { rela ->
                                rela.substring(1, rela.length - 1)
                            }
                        }
                    return@use "200"
                }
        } catch (e: Exception) {
            return@coroutineScope e.message.toString()
        }
    }
}