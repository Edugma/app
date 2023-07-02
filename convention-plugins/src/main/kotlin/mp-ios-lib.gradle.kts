plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()
}
