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
                implementation(compose.uiUtil)

                implementation(projects.shared.core.api)
                implementation(projects.shared.core.storage)
                api(projects.shared.core.icons)
                api(projects.shared.core.resources)
                implementation(libs.imageLoader)
                implementation(libs.kottie)
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
