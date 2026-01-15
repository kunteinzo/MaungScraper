package org.example

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.coroutines.executeAsync

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
            return@coroutineScope rp.body.string()
        }
    }
}