plugins {
    id("mp-compose-lib")
    id("mp-resource-lib")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.animation)
                implementation(compose.material)
                implementation(libs.composeMP.utils)

                implementation(projects.shared.core.api)
                implementation(projects.shared.core.storage)
                api(projects.shared.core.icons)
                api(projects.shared.core.resources)
                implementation(libs.imageLoader)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.lottie)
                implementation(libs.compose.activity)
            }
        }
    }
}

android {
    namespace = "io.edugma.core.designSystem"
    resourcePrefix("core_ed_")
}
