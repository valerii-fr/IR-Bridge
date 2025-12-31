import dev.nordix.irbridge.build_logic.libs

plugins {
    id("dev.nordix.irbridge.build_logic.library")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
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
            }
        }
    }
}


android {
    namespace = "dev.nordix.irbridge.ui"
}
