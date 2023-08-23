@file:Suppress("UNUSED_VARIABLE")
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("mp-common-lib")
    id("org.jetbrains.compose")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                //implementation(platform(libs.compose.bom))
                //implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
//                implementation("dev.icerock.moko:resources:0.23.0")
//                implementation("dev.icerock.moko:resources-compose:0.23.0")
                implementation(libs.moko.resources)
                implementation(libs.moko.resourcesCompose)
                //implementation(compose.preview)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                // debugImplementation
                implementation(libs.compose.uiTooling)
                implementation(libs.compose.uiToolingPreview)
//                implementation("androidx.compose.ui:ui-tooling:1.4.0")
//                implementation("androidx.compose.ui:ui-tooling-preview:1.4.0")
            }
        }

        val iosMain by getting {
            dependencies {
            }
        }
    }
}
