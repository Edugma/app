import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    //alias(libs.plugins.android.application) apply false
    //alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktorfit) apply false

    alias(libs.plugins.jetbrains.multiplatform).apply(false)
    alias(libs.plugins.jetbrains.composePlugin).apply(false)
    alias(libs.plugins.jetbrains.compose.compiler).apply(false)
    alias(libs.plugins.cocoapods).apply(false)
    alias(libs.plugins.android.application).apply(false)
    // alias(libs.plugins.libres).apply(false)
    // alias(libs.plugins.buildConfig).apply(false)

    // TODO https://github.com/JetBrains/compose-multiplatform/issues/4773
    id("com.edugma.android-app").apply(false)
    id("com.edugma.android-lib").apply(false)
    id("com.edugma.mp-web-js-lib").apply(false)
    id("mp-android-lib").apply(false)
    id("mp-common-lib").apply(false)
    id("mp-compose-lib").apply(false)
    id("mp-feature-lib").apply(false)
    id("mp-ios-lib").apply(false)
    id("mp-lint").apply(false)
    id("mp-resource-lib").apply(false)
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = libs.versions.java.get()
    }
}
