@file:Suppress("UnstableApiUsage")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    val androidGradleVersion = "7.4.0"
    val kotlinVersion = "1.7.10"

    plugins {
        id("com.android.application") version androidGradleVersion apply false
        id("com.android.library") version androidGradleVersion  apply false
        kotlin("android") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
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

include(":app")

includeSubmodules(
    "data",
    "domain",
    "features",

    deep = 4,
)

fun includeSubmodules(vararg projectPaths: String, deep: Int = 1) {
    require(deep >= 1)
    projectPaths.forEach { path ->
        val childFiles = File(path).listFiles() ?: return@forEach
        childFiles.forEach { subproject ->
            if (subproject.isDirectory && File(subproject, "build.gradle.kts").exists()) {
                val projectName = subproject.path.replace('\\', ':')
                include(projectName)
            }
            if (deep != 1) {
                includeSubmodules(subproject.path, deep = deep - 1)
            }
        }
    }
}


