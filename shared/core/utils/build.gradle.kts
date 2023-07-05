plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(projects.shared.core.arch)
                implementation(libs.kermit)
            }
        }
    }
}

android {
    namespace = "io.edugma.core.utils"
    resourcePrefix("core_utils_")
}
