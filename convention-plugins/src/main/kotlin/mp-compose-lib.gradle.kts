@file:Suppress("UNUSED_VARIABLE")
import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("mp-common-lib")
    id("org.jetbrains.compose")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                //implementation(platform(libs.compose.bom))
                //implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
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

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        androidMain {
            dependencies {
                // debugImplementation
                implementation(libs.compose.uiTooling)
                implementation(libs.compose.uiToolingPreview)
//                implementation("androidx.compose.ui:ui-tooling:1.4.0")
//                implementation("androidx.compose.ui:ui-tooling-preview:1.4.0")
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}


// TODO Compose Multiplatform > 1.5.11
//tasks.withType<KotlinCompile>().configureEach {
//    kotlinOptions {
//        freeCompilerArgs += listOf(
//            "-P",
//            "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=" +
//                "$rootDir${File.separator}configs${File.separator}compose${File.separator}compose_compiler_config.conf"
//        )
//    }
//}
