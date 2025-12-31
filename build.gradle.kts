import io.gitlab.arturbosch.detekt.Detekt

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.androidx.room) apply false
}

val projectSource = file(projectDir)
val configFile = files("$projectDir/detekt/detekt.yml")
val baselineFile = file("$projectDir/detekt/baseline.xml")
val kotlinFiles = "**/*.kt"
val resourceFiles = "**/resources/**"
val buildFiles = "**/build/**"
val gradle = "**/.gradle/**"

detekt {
    parallel = true
    config.setFrom("detekt/detekt.yml")
    buildUponDefaultConfig = false
    allRules = false
    disableDefaultRuleSets = false
    debug = false
    ignoreFailures = false
    basePath = projectDir.path
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

tasks.register<Detekt>("detektAll") {
    description = "Custom Detekt build for all modules"
    parallel = true
    ignoreFailures = false
    autoCorrect = true
    setSource(projectSource)
    config.setFrom(configFile)
    include(kotlinFiles)
    exclude(resourceFiles, buildFiles, gradle, "**test/**")
    reports {
        html {
            enabled = true
        }
    }
}