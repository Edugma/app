plugins {
    id("mp-compose-lib")
    alias(libs.plugins.kotlinx.atomic)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.designSystem)
                api(projects.navigation.core)

                implementation(libs.kotlinx.dateTime)
            }
        }
    }
}

android {
    namespace = "io.edugma.core.navigation"
    resourcePrefix("core_nav_")
}
