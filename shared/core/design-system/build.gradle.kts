plugins {
    id("mp-compose-lib")
    id("mp-resource-lib")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.animation)
                implementation(compose.material)
                implementation(compose.uiUtil)

                implementation(projects.shared.core.api)
                implementation(projects.shared.core.storage)
                api(projects.shared.core.icons)
                api(projects.shared.core.resources)
                implementation(libs.imageLoader)
                implementation(libs.kottie)
                implementation(libs.okio.fakefilesystem)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.lottie)
                implementation(libs.compose.activity)
            }
        }

        iosArm64 {
            compilations.getByName("main") {
                val Lottie by cinterops.creating {
                    defFile("src/nativeInterop/cinterop/Lottie.def")
                    val path = "$rootDir/vendor/Lottie.xcframework/ios-arm64"
                    compilerOpts("-F$path", "-framework", "Lottie", "-rpath", path)
                    extraOpts += listOf("-compiler-option", "-fmodules")
                }
            }
        }

        listOf(
            iosX64(),
            iosSimulatorArm64()
        ).forEach {
            it.compilations.getByName("main") {
                val Lottie by cinterops.creating {
                    defFile("src/nativeInterop/cinterop/Lottie.def")
                    val path = "$rootDir/vendor/Lottie.xcframework/ios-arm64_x86_64-simulator"
                    compilerOpts("-F$path", "-framework", "Lottie", "-rpath", path)
                    extraOpts += listOf("-compiler-option", "-fmodules")
                }
            }
        }
    }
}

android {
    namespace = "com.edugma.core.designSystem"
    resourcePrefix("core_ed_")
}
