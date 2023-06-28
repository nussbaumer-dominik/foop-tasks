plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "at.ac.tuwien.foop"
version = "1.0.0"

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(KotlinX.serialization.json)
                implementation(Ktor.client.core)
                implementation(Ktor.client.cio)
                implementation(Ktor.plugins.serialization.kotlinx.json)
                implementation(Ktor.client.contentNegotiation)
            }
        }
    }
}
