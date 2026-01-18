package mg.scrape

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Scraper {

    fun search(query: String, onResponse: OnResponse) {
        try {
            HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create("${BASE_URL}/search/$query")).build(),
                HttpResponse.BodyHandlers.ofString()
            )
        } catch (e: Exception) {
            onResponse.onResponse(e)
        }
    }

    fun watch(link: String) {

    }

    fun interface OnResponse {
        fun onResponse(exception: Exception?)
    }

    companion object {
        private const val BASE_URL = "https://www.xnxx.com"
    }
}