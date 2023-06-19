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
                implementation(compose.ui)
                //implementation(compose.runtime)
                //implementation(compose.foundation)
                //implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.preview)
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
//                implementation(libs.androidx.appcompat)
//                implementation(libs.androidx.activityCompose)
//                implementation(libs.compose.uitooling)
//                implementation(libs.kotlinx.coroutines.android)
            }
        }

        val iosMain by getting {
            dependencies {
            }
        }

    }
}
