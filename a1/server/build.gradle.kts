plugins {
    id("org.jetbrains.kotlin.jvm")
    id("io.ktor.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "at.ac.tuwien.foop"
version = "1.0.0"

dependencies {
    implementation(Ktor.server.core)
    implementation(Ktor.plugins.serialization.kotlinx.json)
    implementation(Ktor.server.websockets)
    implementation(Ktor.server.netty)

    implementation("ch.qos.logback:logback-classic:_")

    implementation(projects.common)
}

application {
    mainClass.set("at.ac.tuwien.foop.ApplicationKt")
}
