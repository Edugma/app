plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.core.arch)
                implementation(projects.shared.core.api)

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kermit)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
            }
        }
    }
}

android {
    namespace = "io.edugma.core.utils"
    resourcePrefix("core_utils_")
}
