package dev.nordix.irbridge.build_logic

val appVersionName: String by gradle.extra
val appVersionCode: Int by gradle.extra

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.android.library")
}

kotlin {
    androidTarget()

    sourceSets {
        all {
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutineApi")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlin.uuid.ExperimentalUuidApi")
            languageSettings.optIn("com.arkivanov.decompose.DelicateDecomposeApi")
        }
        commonMain {
            dependencies {
                implementation(project(":core"))
                implementation(libs.findBundle("koin").get())
                implementation(libs.findLibrary("kotlinx.serialization.json").get())
            }
        }
        commonTest.dependencies {
            implementation(libs.findLibrary("kotlin.test").get())
        }
    }

    compilerOptions {
        freeCompilerArgs.set(listOf("-Xannotation-default-target=param-property"))
    }

}

android {
    compileSdk = libs.findVersion("android.compileSdk").get().requiredVersion.toInt()
    version = appVersionName

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "APP_VERSION", "\"${project.version}\"")
        buildConfigField("String", "APP_VERSION_CODE", "\"${appVersionCode}\"")
        buildConfigField("String", "APP_NAME", "\"${project.rootProject.name}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        minSdk = libs.findVersion("android.minSdk").get().requiredVersion.toInt()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            buildConfigField("Boolean", "IS_DEBUG", "false")
            isMinifyEnabled = false
        }
        debug {
            buildConfigField("Boolean", "IS_DEBUG", "true")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
