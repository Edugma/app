plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.libs.navigation.core)
                api(projects.shared.core.navigation)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.kotlinx.dateTime)
            }
        }
    }
}

android.namespace = "io.edugma.core.arch"

