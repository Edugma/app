@file:Suppress("UnstableApiUsage", "UNUSED_VARIABLE")

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("mp-compose-lib")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.koin.core)
                //implementation("io.insert-koin:koin-core:3.4.0")
                implementation(project(":shared:core:arch"))
                implementation(project(":shared:core:utils"))
            }
        }
    }
}
