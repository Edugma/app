plugins {
    id("mp-compose-lib")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.essenty.lifecycle)
                implementation(libs.essenty.instanceKeeper)
                implementation(libs.kotlinx.coroutines.core)
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

