group = "at.ac.tuwien.foop"
version = "1.0.0"

allprojects {
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("multiplatform") apply false
    id("org.jetbrains.compose") apply false
}