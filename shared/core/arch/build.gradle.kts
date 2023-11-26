plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.core.api)
                api(projects.libs.navigation.core)
                api(projects.shared.core.navigation)
                api(libs.essenty.instanceKeeper)
                api(libs.essenty.lifecycle)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.kotlinx.dateTime)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.viewmodel.compose)
            }
        }
    }
}

android.namespace = "io.edugma.core.arch"

