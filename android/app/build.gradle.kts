import com.android.build.api.dsl.ApplicationBuildType
import org.gradle.accessors.dm.LibrariesForLibs
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("io.edugma.android-app")
    kotlin("android")
    id("lint")
}
// TODO signing
// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

val versionsProperties = Properties()
versionsProperties.load(FileInputStream(rootProject.file("configs/versions.properties")))

android {
    namespace = "io.edugma.android"

    defaultConfig {
        applicationId = "io.edugma.android"
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = versionsProperties.getProperty("versionCode").toInt()
        versionName = versionsProperties.getProperty("versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        // We use a bundled debug keystore, to allow debug builds from CI to be upgradable
        named("debug") {
            storeFile = rootProject.file("configs/debug.jks")
            storePassword = "edugma"
            keyAlias = "edugmadebugkey"
            keyPassword = "edugma"
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"

            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }
        maybeCreate("qa").apply {
            matchingFallbacks.add("release")
            applicationIdSuffix = ".qa"

            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":shared:app"))
    implementation(project(":android:resources"))
    implementation(projects.shared.core.navigation)
    implementation(projects.shared.core.api)
    //implementation(projects.android.schedule.appwidget)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.activity)

    implementation(libs.koin.android)
    implementation(libs.androidx.splashScreen)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.viewmodel.compose)
    implementation(libs.essenty.instanceKeeper)
    //implementation("androidx.compose.runtime:runtime-tracing:1.0.0-alpha03")

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.junit.ext)
    androidTestImplementation(libs.test.espressoCore)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.uiTest)
}
