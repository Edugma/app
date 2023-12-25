import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

val copyJsResources = tasks.create("copyJsResourcesWorkaround", Copy::class.java) {
    from(projects.shared.app.dependencyProject.file("src/commonMain/resources"))
    into("build/processedResources/js/main")
}

//val copyWasmResources = tasks.create("copyWasmResourcesWorkaround", Copy::class.java) {
//    from(projects.shared.app.dependencyProject.file("src/commonMain/resources"))
//    into("build/processedResources/wasmJs/main")
//}

afterEvaluate {
    project.tasks.getByName("jsProcessResources").finalizedBy(copyJsResources)
    //project.tasks.getByName("wasmJsProcessResources").finalizedBy(copyWasmResources)
}

kotlin {
    js(IR) {
        moduleName = "edugma"
        browser {
            commonWebpackConfig {
                outputFileName = "edugma.js"
            }
        }
        binaries.executable()
    }

//    wasmJs {
//        moduleName = "edugma"
//        browser {
//            commonWebpackConfig {
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    // Uncomment and configure this if you want to open a browser different from the system default
//                    // open = mapOf(
//                    //     "app" to mapOf(
//                    //         "name" to "google chrome"
//                    //     )
//                    // )
//
//                    static = (static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(project.rootDir.path)
//                        add(project.rootDir.path + "/shared/")
//                        add(project.rootDir.path + "/nonAndroidMain/")
//                        add(project.rootDir.path + "/webApp/")
//                    }
//                }
//            }
//        }
//        binaries.executable()
//    }

    sourceSets {
        // Common for jsMain and wasmJsMain
        val webCommonMain by creating {
            dependencies {
                implementation(projects.shared.app)
                implementation(projects.shared.core.api)
                implementation(projects.shared.core.navigation)

                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.essenty.instanceKeeper)
            }
        }
        val jsMain by getting {
            dependsOn(webCommonMain)

            dependencies {

            }
        }
//        val wasmJsMain by getting {
//            dependsOn(webCommonMain)
//        }
    }
}

compose.experimental {
    web.application {}
}
