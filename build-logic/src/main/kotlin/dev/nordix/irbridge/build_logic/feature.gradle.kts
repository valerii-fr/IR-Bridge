package dev.nordix.irbridge.build_logic

plugins {
    id("dev.nordix.irbridge.build_logic.common_ui")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:mvi"))
                implementation(project(":core:ui"))
                implementation(libs.findLibrary("androidx.activity.compose").get())
                implementation(libs.findLibrary("androidx.lifecycle.viewmodelCompose").get())
                implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                implementation(libs.findLibrary("decompose.extensions.compose").get())
                implementation(libs.findBundle("orbit").get())
            }
        }
    }
}
