@file:Suppress("UnstableApiUsage")

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.android.library")
    kotlin("android")
    id("lint")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

android {
    compileSdk = Config.compileSdk

    defaultConfig {
        minSdk = Config.minSdk

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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    packagingOptions {
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

dependencies {
    coreLibraryDesugaring(libs.desugaring)
}
