

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
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(project(":shared:core:arch"))
                implementation(project(":shared:core:utils"))
            }
        }
    }
}
