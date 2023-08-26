plugins {
    id("mp-common-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.core.api)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.appCompat)
            }
        }
    }
}

android {
    namespace = "io.edugma.core.system"
    resourcePrefix("core_system_")
}
