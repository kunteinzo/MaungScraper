package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main() {
    get("https://api.ipify.org") { r, e ->
        println(r?.body())
    }
}

fun get(url: String, getResponse: GetResponse) {
    try {
        HttpClient.newHttpClient().use { client ->
            client.send(
                HttpRequest.newBuilder(URI.create(url)).build(),
                HttpResponse.BodyHandlers.ofString()
            )?.let { response ->
                getResponse.onResponse(response, null)
            }
        }
    } catch (e: Exception) {
        getResponse.onResponse(null, e)
    }
}

fun interface GetResponse {
    fun onResponse(response: HttpResponse<String?>?, e: Exception?)
}