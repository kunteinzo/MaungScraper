plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)
    implementation("org.jsoup", "jsoup", "1.22.1")
    implementation("com.google.code.gson", "gson", "2.13.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

version = "1.0-alpha"