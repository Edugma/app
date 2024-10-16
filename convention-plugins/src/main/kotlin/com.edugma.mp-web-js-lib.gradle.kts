plugins {
    kotlin("multiplatform")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    js(IR) {
        browser {
//            dceTask {
//                keep("ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io")
//            }
        }
    }

    // TODO Wasm js
//    wasmJs {
//        browser()
//    }

    sourceSets {
        val webCommonMain by creating {
            dependsOn(commonMain.get())
        }

        val jsMain by getting {
            dependsOn(webCommonMain)
            dependencies {
            }
        }

//    val wasmJsMain by getting {
//        dependsOn(webCommonMain)
//    }
    }
}
