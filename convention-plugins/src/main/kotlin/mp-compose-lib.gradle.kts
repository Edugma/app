@file:Suppress("UNUSED_VARIABLE")
import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("mp-common-lib")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    sourceSets {
        all {
            languageSettings.optIn("androidx.compose.material3.ExperimentalMaterial3Api")
        }

        commonMain {
            dependencies {
                //implementation(project.dependencies.platform(libs.compose.bom))
                //implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
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

composeCompiler {
    stabilityConfigurationFile.set(
        File(
            "$rootDir/configs/compose/compose_compiler_config.conf"
        )
    )
}
