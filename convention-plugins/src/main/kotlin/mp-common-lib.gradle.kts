@file:Suppress("UNUSED_VARIABLE")

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    kotlin("multiplatform")
    id("mp-android-lib")
    id("mp-ios-lib")
    id("com.edugma.mp-web-js-lib")
    id("mp-lint")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
//    compilerOptions {
//        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
//    }
    task("testClasses")
    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kermit)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val mobileCommonMain by creating {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependsOn(mobileCommonMain)
        }

        iosMain {
            dependsOn(mobileCommonMain)
        }
    }
}
