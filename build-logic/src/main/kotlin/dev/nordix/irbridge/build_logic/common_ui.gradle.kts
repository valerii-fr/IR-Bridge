package dev.nordix.irbridge.build_logic

import gradle.kotlin.dsl.accessors._2f2ad61e350a15ce1d3dbb9a99cd4188.kspCommonMainMetadata
import gradle.kotlin.dsl.accessors._2f2ad61e350a15ce1d3dbb9a99cd4188.kspDebug

plugins {
    id("dev.nordix.irbridge.build_logic.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
    id("com.google.devtools.ksp")
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(compose.preview)
                implementation(compose.uiTooling)
            }
        }
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.findLibrary("material.icons.extended").get())
                implementation(project(":common-ui"))
                implementation(libs.findLibrary("compose.destinations").get())
            }
        }
    }
}

dependencies {
    val composeDestinations = libs.findLibrary("compose.destinations.ksp").get()
    kspCommonMainMetadata(composeDestinations)
    kspDebug(composeDestinations)
    add("kspAndroid", composeDestinations)
}
