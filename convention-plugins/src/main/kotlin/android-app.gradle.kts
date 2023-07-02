@file:Suppress("UnstableApiUsage")

//import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.android.application")
    kotlin("android")
    id("lint")
}

// https://github.com/gradle/gradle/issues/15383
//val libs = the<LibrariesForLibs>()

android {
    compileSdk = Config.compileSdk

    defaultConfig {
        applicationId = "io.edugma.android"
        minSdk = Config.minSdk
        targetSdk = Config.targetSdkVersion
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = Config.androidTestInstrumentation
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"//libs.versions.composeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    resourcePrefix = getPrefix()
}

fun getPrefix(): String {
    val parentName = project.parent?.name ?: ""
    val parentBlockList = listOf("data", "domain", "features")
    val parentPrefix = if (parentName in parentBlockList) {
        ""
    } else {
        parentName.split("_")
            .joinToString(separator = "_", postfix = "_")
    }

    val prefix = if (project.name.length <= 5) {
        project.name + "_"
    } else {
        project.name.split("_")
            .joinToString(separator = "_", postfix = "_") { it.take(3) }
    }

    return parentPrefix + prefix
}
