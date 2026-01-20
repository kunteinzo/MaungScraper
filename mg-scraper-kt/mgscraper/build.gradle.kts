import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.2.0"
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.10.2")
    implementation("com.squareup.okhttp3", "okhttp", "5.3.2")
    implementation("com.squareup.okhttp3", "okhttp-coroutines", "5.3.2")
    implementation("org.jsoup", "jsoup", "1.22.1")
    implementation("com.google.code.gson", "gson", "2.13.2")
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

group = "tz.kunteinzo.mg.scraper"
version = "1.0-SNAPSHOT"