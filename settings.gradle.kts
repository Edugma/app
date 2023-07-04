@file:Suppress("UnstableApiUsage")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    //repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "edugma"

includeBuild("convention-plugins")


//include(":navigation:core")
//
//include(":core:android-resources")
//include(":core:arch")
//include(":core:design-system")
//include(":core:icons")
//include(":core:navigation")
//include(":core:network")
//include(":core:ui")
//include(":core:utils")
//
//include(":domain:base-domain")
//include(":domain:nodes")
//include(":domain:account")
//
//include(":data:base")
//include(":data:nodes")
//include(":data:account")
//
//include(":features:account")
//include(":features:app")
//include(":features:home")
//include(":features:nodes")
//
//include(":features:misc:menu")
//include(":features:misc:settings")
//
//include(":features:schedule:calendar")
//include(":features:schedule:daily")
//include(":features:schedule:data")
//include(":features:schedule:domain")
//include(":features:schedule:elements")
//include(":features:schedule:free-place")
//include(":features:schedule:history")
//include(":features:schedule:lessons-review")
//include(":features:schedule:menu")
//include(":features:schedule:schedule_info")
//include(":features:schedule:sources")

includeSubmodules(
    "data",
    "domain",
    "features",
    "core",
    "navigation",

    deep = 4,
)
include(":shared")
include(":androidApp")

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




