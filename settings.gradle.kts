@file:Suppress("UnstableApiUsage")
pluginManagement {
    repositories {
        google {
            content {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/maven/") }
    }
}

dependencyResolutionManagement {
    //repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        // kottie -> ktor 3.0.0-wasm2
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/maven/") }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "edugma"

includeBuild("convention-plugins")

includeSubmodules(
    "libs",
    "shared",
    "android",
    "web",

    deep = 4,
)
include(":sharedIos")

fun includeSubmodules(vararg projectPaths: String, deep: Int = 1) {
    require(deep >= 1)

    projectPaths.forEach { path ->
        val file = File(rootDir, path)
        val childFiles = file.listFiles() ?: return@forEach
        childFiles.forEach { subproject ->
            if (subproject.isDirectory) {
                if (File(subproject, "build.gradle.kts").exists()) {
                    val projectName = subproject.absolutePath
                        .removePrefix(rootDir.absolutePath)
                        .replace(File.separator, ":")
                    include(projectName)
                }
                if (deep != 1) {
                    includeSubmodules(
                        subproject.absolutePath
                            .removePrefix(rootDir.absolutePath),
                        deep = deep - 1
                    )
                }
            }
        }
    }
}




