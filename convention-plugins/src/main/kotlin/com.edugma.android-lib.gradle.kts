import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

kotlin.android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    minSdk = libs.versions.minSdk.get().toInt()
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
        freeCompilerArgs.add("-Xjdk-release=${libs.versions.java.get()}")
    }
    packaging.resources {
        excludes.add("META-INF/**")
        pickFirsts.add("MR/**")
    }
}
