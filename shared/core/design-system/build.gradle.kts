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
                implementation(libs.compottie)
                implementation(libs.compottie.resources)
                implementation(libs.compottie.dot)
                implementation(libs.compottie.network)
                implementation(libs.okio.fakefilesystem)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.compose.activity)
            }
        }
    }
}

android {
    namespace = "com.edugma.core.designSystem"
    resourcePrefix("core_ed_")
}
