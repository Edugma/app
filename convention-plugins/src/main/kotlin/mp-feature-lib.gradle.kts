@file:Suppress("UnstableApiUsage")

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("mp-compose-lib")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()


kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.koin.core)
                implementation(project(":core:arch"))
                implementation(project(":core:utils"))
            }
        }
    }
}
