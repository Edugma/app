plugins {
    id("mp-compose-lib")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.essenty.lifecycle)
                implementation(libs.essenty.instanceKeeper)
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.compose.activity)
            }
        }
    }
}

android.namespace = "io.edugma.navigation.core"

