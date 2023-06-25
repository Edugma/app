plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.navigation.core)
                api(projects.core.navigation)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.kotlinx.dateTime)
            }
        }
    }
}

android.namespace = "io.edugma.core.arch"

