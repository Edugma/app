plugins {
    id("mp-compose-lib")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.compose.activity)
            }
        }

        val iosMain by getting {
            dependencies {
            }
        }
    }
}

android.namespace = "io.edugma.navigation.core"

