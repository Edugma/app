@file:Suppress("UnstableApiUsage")

include(":features:schedule:free_place")


include(":features:schedule:daily")
include(":features:schedule:menu")
include(":features:schedule:elements")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":features:schedule:history")


include(":features:schedule:appwidget")


include(":features:schedule:sources")
include(":features:schedule:lessons_review")
include(":features:schedule:calendar")
include(":features:schedule:schedule_info")


pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    val androidGradleVersion = "7.2.1"
    val kotlinVersion = "1.6.21"

    plugins {
        id("com.android.application") version androidGradleVersion apply false
        id("com.android.library") version androidGradleVersion  apply false
        kotlin("android") version "1.6.21" apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.jetbrains.kotlin.jvm") version "1.6.10"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "edugma"

include(":app")


include(":data")
include(":data:base")
include(":data:schedule")
include(":data:account")
include(":data:nodes")



include(":domain")
include(":domain:base")
include(":domain:schedule")
include(":domain:account")
include(":domain:nodes")



include(":features")
include(":features:base")
include(":features:base:core")
include(":features:base:navigation")
include(":features:base:elements")
include(":features:schedule")
include(":features:account")
include(":features:home")
include(":features:nodes")

include(":features:misc")
include(":features:misc:menu")
include(":features:misc:settings")


