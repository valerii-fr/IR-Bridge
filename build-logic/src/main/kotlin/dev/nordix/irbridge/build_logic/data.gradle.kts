package dev.nordix.irbridge.build_logic

plugins {
    id("dev.nordix.irbridge.build_logic.library")
    id("androidx.room")
    id("com.google.devtools.ksp")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.findLibrary("androidx.room.ktx").get())
        }
        commonMain {
            dependencies {
                implementation(libs.findLibrary("androidx.room.runtime").get())
                implementation(libs.findLibrary("sqlight.bundled").get())
            }
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    val roomCompiler = libs.findLibrary("androidx.room.compiler").get()
    kspCommonMainMetadata(roomCompiler)
    kspDebug(roomCompiler)
    add("kspAndroid", roomCompiler)
}
