plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.core.arch)

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kermit)
            }
        }
    }
}

android {
    namespace = "io.edugma.core.utils"
    resourcePrefix("core_utils_")
}
