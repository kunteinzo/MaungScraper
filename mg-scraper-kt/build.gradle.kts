plugins {
    kotlin("jvm") version "2.2.0"
    application
}

group = "com.kunteinzo.scraper"
version = "1.0-SNAPSHOT"

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
    implementation("com.google.code.gson", "gson", "2.13.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
application {
    mainClass = "com.kunteinzo.scraper.MainKt"
}