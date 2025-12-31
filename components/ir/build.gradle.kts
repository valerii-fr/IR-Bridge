plugins {
    id("dev.nordix.irbridge.build_logic.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
        }
    }
}

android {
    namespace = "dev.nordix.irbridge.ir"
}