@file:Suppress("UnstableApiUsage")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "edugma"

includeBuild("convention-plugins")
include(":androidApp")
includeSubmodules(
    "data",
    "domain",
    "features",
    "core",
    "navigation",

    deep = 4,
)

fun includeSubmodules(vararg projectPaths: String, deep: Int = 1) {
    require(deep >= 1)
    projectPaths.forEach { path ->
        val childFiles = File(path).listFiles() ?: return@forEach
        childFiles.forEach { subproject ->
            if (subproject.isDirectory && File(subproject, "build.gradle.kts").exists()) {
                val projectName = subproject.path
                    .replace('\\', ':') //for windows
                    .replace('/', ':') //for mac
                include(projectName)
            }
            if (deep != 1) {
                includeSubmodules(subproject.path, deep = deep - 1)
            }
        }
    }
}


