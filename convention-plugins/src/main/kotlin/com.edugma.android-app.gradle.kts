import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.android.application")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // excludes.add("META-INF/**")
        }
    }
}
