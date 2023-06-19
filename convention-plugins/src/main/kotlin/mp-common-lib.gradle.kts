import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("mp-android-lib")
    id("mp-ios-lib")
    id("lint")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.uuid)
                implementation(libs.kermit)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
        }

        val iosMain by getting {
        }

    }
}
