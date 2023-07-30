@file:Suppress("UNUSED_VARIABLE")

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("mp-android-lib")
    id("mp-ios-lib")
    id("mp-lint")
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
//                implementation("com.benasher44:uuid:0.7.0")
//                implementation("co.touchlab:kermit:2.0.0-RC4")
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
