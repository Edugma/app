plugins {
    val androidGradleVersion = "7.4.0"
    val kotlinVersion = "1.8.0"
    val ktlintPluginVersion = "3.13.0"

//    id("com.android.application") version androidGradleVersion apply false
//    id("com.android.library") version androidGradleVersion  apply false
//    kotlin("android") version kotlinVersion apply false
//    kotlin("plugin.serialization") version kotlinVersion apply false
//    id("org.jetbrains.kotlin.jvm") version kotlinVersion
//    id("org.jmailen.kotlinter") version ktlintPluginVersion apply false

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
