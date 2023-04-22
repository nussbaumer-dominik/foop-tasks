plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "at.ac.tuwien.foop"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(KotlinX.serialization.json)
            }
        }
    }
}
