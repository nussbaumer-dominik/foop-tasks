import de.fayard.refreshVersions.core.FeatureFlag
import de.fayard.refreshVersions.core.StabilityLevel

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.51.0"
}

refreshVersions {
    rejectVersionIf {
        candidate.stabilityLevel != StabilityLevel.Stable
    }

    featureFlags {
        enable(FeatureFlag.LIBS)
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "a1"

include("common")
include("server")
include("desktop")
