@file:Suppress("UNUSED_VARIABLE")
import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
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

        androidMain {
            dependencies {
                implementation(libs.compose.uiToolingPreview)
            }
        }

        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.uiToolingPreview)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    dependencies {
        debugImplementation(libs.compose.uiTooling)
    }
}

composeCompiler {
    stabilityConfigurationFile.set(
        file("$rootDir/configs/compose/compose_compiler_config.conf")
    )
    featureFlags.set(
        listOf(
            ComposeFeatureFlag.StrongSkipping, ComposeFeatureFlag.IntrinsicRemember,
        ),
    )
}
