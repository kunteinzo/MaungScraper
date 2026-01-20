package com.kunteinzo.scraper

import kotlinx.coroutines.runBlocking
import tz.kunteinzo.mg.scraper.Xnxx

fun main() {
    runBlocking {
        println(Xnxx().search("japan"))
    }
}