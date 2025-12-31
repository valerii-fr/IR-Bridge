import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val versionPropsFile = file("version.properties")
val versionProps = Properties().apply {
    if (versionPropsFile.exists()) {
        versionPropsFile.inputStream().use { load(it) }
    } else {
        throw GradleException("Missing version.properties file!")
    }
}

val major = versionProps.getProperty("major")?.toIntOrNull() ?: 0
val minor = versionProps.getProperty("minor")?.toIntOrNull() ?: 0
val patch = versionProps.getProperty("patch")?.toIntOrNull() ?: 0
val appVersionName = "$major.$minor.$patch"
val appVersionCode = major * 100000 + minor * 10000 + patch

gradle.extra["appVersionName"] = appVersionName
gradle.extra["appVersionCode"] = appVersionCode

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.3.0"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.bundles.koin)

            implementation(project(":core"))
            implementation(project(":common-ui"))
            implementation(project(":components:ble"))
            implementation(project(":components:ir"))
            implementation(project(":components:remotes"))
            
            implementation(project(":feature:remotes"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "dev.nordix.irbridge"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = namespace
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionName = appVersionName
        versionCode = appVersionCode
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

android.applicationVariants.all {
    outputs.all {
        val vName = versionName
        val appId = applicationId
        val fileName = "${appId}-${name}-v${vName}.apk"

        (this as com.android.build.gradle.internal.api.ApkVariantOutputImpl).outputFileName = fileName
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    withDependencies {
        forEach { dep ->
            val ext = dep as? ExternalModuleDependency ?: return@forEach
            if (ext.group?.contains("dev.nordix") == true) {
                ext.isChanging = true
            }
        }
    }
}