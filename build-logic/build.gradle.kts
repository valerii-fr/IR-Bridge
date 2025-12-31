plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://androidx.dev/storage/compose-compiler/repository")
    maven("https://jitpack.io")
    maven("https://dl.google.com/dl/android/maven2/")
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.room.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.multiplatform.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.kotlin.compose.gradlePlugin)
    implementation(libs.kotlin.serialization.gradlePlugin)
}
