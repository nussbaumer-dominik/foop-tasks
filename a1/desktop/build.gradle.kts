import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
}

group = "at.ac.tuwien.foop"
version = "1.0.0"

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }

    sourceSets {
        getByName("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation(projects.common)

                implementation(Ktor.client.core)
                implementation(Ktor.client.cio)
                implementation(Ktor.plugins.serialization.kotlinx.json)
                implementation("io.ktor:ktor-client-websockets:_")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "desktop"
            packageVersion = "1.0.0"
        }
    }
}
