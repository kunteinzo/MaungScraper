package tz.kunteinzo.mg.scraper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tz.kunteinzo.mg.scraper.models.XnxxRelated
import tz.kunteinzo.mg.scraper.models.XnxxResponse
import tz.kunteinzo.mg.scraper.models.XnxxVideo
import tz.kunteinzo.mg.scraper.models.XnxxWatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class Xnxx {
    companion object {
        private const val BASE_URL = "https://www.xnxx.com"
    }

    fun interface OnSearchResponse {
        fun onResponse(xnxxResponse: XnxxResponse)
    }
    
    fun interface OnWatchResponse {
        fun onResponse(xnxxWatch: XnxxWatch)
    }

    private fun req(url: String) = OkHttpClient.Builder().build().newCall(Request.Builder()
        .url(url)
        .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.32 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.32")
        .build())

    suspend fun search(query: String): XnxxResponse = coroutineScope {
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
                            xnxx.sfw = it.attr("data-sfwthumb")
                            xnxx.pvv = it.attr("data-pvv")
                            xnxx.mzl = it.attr("data-mzl")
                        }

                        array.add(xnxx)
                    }
                    root.selectFirst(".pagination")?.selectFirst(".last-page")?.attr("href")?.let {
                        xnxxResp.pages = it.substring(it.lastIndexOf("/") + 1).toIntOrNull() ?: 0
                    }

                    xnxxResp.list = array
                }
                xnxxResp.message = "Fetch Success"
            }
        } catch (e: Exception) {
            xnxxResp.message = e.message.toString()
        }
        return@coroutineScope xnxxResp
    }

    suspend fun watch(link: String): XnxxWatch = coroutineScope {
        val xnxxWatch = XnxxWatch()
        try {
            req("${BASE_URL}$link").execute().use { resp ->
                    val root = Jsoup.parse(resp.body.string())
                    root.getElementsByTag("script").find { it.data().contains("VideoHLS") }?.data()?.let { player ->
                        "VideoHLS.*'".toRegex().find(player)?.value?.let { hls ->
                            xnxxWatch.hls = hls.substring(10, hls.length - 1)
                        }
                        "ThumbUrl.*'".toRegex().find(player)?.value?.let { thumbUrl ->
                            xnxxWatch.thumb = thumbUrl.substring(10, thumbUrl.length - 1)
                        }
                        "ThumbUrl169.*'".toRegex().find(player)?.value?.let { thumbUrl169 ->
                            xnxxWatch.thumb169 = thumbUrl169.substring(13, thumbUrl169.length - 1)
                        }
                        "ThumbSlideMinute.*'".toRegex().find(player)?.value?.let { slideMinute ->
                            xnxxWatch.slideMinute = slideMinute.substring(18, slideMinute.length - 1)
                        }
                        "ThumbSlideBig.*'".toRegex().find(player)?.value?.let { slideBig ->
                            xnxxWatch.slideBig = slideBig.substring(15, slideBig.length - 1)
                        }
                        "ThumbSlide.*'".toRegex().find(player)?.value?.let { slide ->
                            xnxxWatch.slide = slide.substring(12, slide.length - 1)
                        }
                    }
                    root.selectFirst(".video-title")?.text()?.trim()?.let { title -> xnxxWatch.title = title }
                    root.selectFirst(".video-description")?.text()?.trim()?.let { desc -> xnxxWatch.description = desc }
                    root.selectFirst(".free-plate")?.let { maker ->
                        xnxxWatch.makerName = maker.text().trim()
                        xnxxWatch.makerLink = maker.attr("href")
                    }
                    root.getElementsByTag("script").find { it.data().contains("video_related=") }?.data()
                        ?.let { related ->
                            "=.*];".toRegex().find(related)?.value?.let { rela ->
                                xnxxWatch.related = Gson().fromJson(rela.substring(1, rela.length - 1).replace("\\/", "/"), object: TypeToken<List<XnxxRelated>>(){})
                            }
                    // return@use xnxxWatch
                }
            }
        } catch (e: Exception) {
            xnxxWatch.description = e.message.toString()
            // return@coroutineScope xnxxWatch
        }
        return@coroutineScope xnxxWatch
    }
    
    fun searchCall(query: String, onSearchResponse: OnSearchResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            val rp = search(query)
            withContext(Dispatchers.Main) {
                onSearchResponse.onResponse(rp)
            }
        }
    }
    
    fun watchCall(link: String, onWatchResponse: OnWatchResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            val rp = watch(link)
            withContext(Dispatchers.Main) {
                onWatchResponse.onResponse(rp)
            }
        }
    }
}