@file:Suppress("UnstableApiUsage")

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("android-lib")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.koin)
    implementation(libs.voyager.bottomSheetNavigator)
    implementation(libs.voyager.tabNavigator)
    debugImplementation(libs.compose.uiTooling)
}
