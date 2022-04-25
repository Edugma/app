@file:Suppress("UnstableApiUsage")

include(":features:schedule:lessons_review")


include(":features:schedule:calendar")


include(":features:schedule:schedule_info")


pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    val androidGradleVersion = "7.1.3"
    val kotlinVersion = "1.6.10"

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


