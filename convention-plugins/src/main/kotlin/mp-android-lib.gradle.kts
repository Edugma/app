import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.edugma.android-lib")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
            freeCompilerArgs.add("-Xjdk-release=${libs.versions.java.get()}")
        }
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        instrumentedTestVariant {
//            sourceSetTree.set(KotlinSourceSetTree.test)
//            dependencies {
//                debugImplementation(libs.androidx.testManifest)
//                implementation(libs.androidx.junit4)
//            }
//        }
    }
}

android {
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources")
        resources.srcDirs("src/commonMain/resources")
    }
}
