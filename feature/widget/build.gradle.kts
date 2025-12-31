plugins {
    id("dev.nordix.irbridge.build_logic.common_ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":components:remotes"))
            implementation(project(":components:ir"))
            implementation(project(":feature:remotes"))
            implementation(libs.bundles.glance)
        }
    }
}

android {
    namespace = "dev.nordix.irbridge.feature.widget"
}
