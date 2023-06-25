plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.domain.base)
                implementation(projects.data.base)
                api(projects.core.icons)
                implementation(libs.imageLoader)
                implementation(libs.androidx.annotations)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.lottie)
                //implementation(libs.androidx.lifecycleRuntime)
                //implementation(libs.androidx.coreKtx)
                //implementation(libs.androidx.appCompat)
            }
        }
    }
}

android {
    namespace = "io.edugma.core.designSystem"
    resourcePrefix("core_ed_")
}
