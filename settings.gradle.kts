pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://androidx.dev/storage/compose-compiler/repository")

        maven {
            metadataSources {
                mavenPom()
                artifact()
            }
            url = uri("https://nexus.nordix.dev/repository/maven-releases/")
        }
    }

}

rootProject.name = "IR-Bridge"

includeBuild("build-logic")

include(":app")

include(":core")
include(":common-ui")

include(":components:ble")
include(":components:ir")
include(":components:remotes")
include(":components:export")

include(":feature:remotes")
include(":feature:widget")
include(":baselineprofile")
