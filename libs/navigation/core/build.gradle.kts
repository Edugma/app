plugins {
    id("mp-compose-lib")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.lifecycle.viewmodel)
                api(libs.androidx.navigation.compose)
                api(libs.androidx.core.bundle)
                implementation(libs.ktorUtils)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.compose.activity)
            }
        }
    }
}

android.namespace = "io.edugma.navigation.core"

