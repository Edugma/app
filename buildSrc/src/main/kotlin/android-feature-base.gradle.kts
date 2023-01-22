@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    kotlin("android")
}

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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
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
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.0")
}