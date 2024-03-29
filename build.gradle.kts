import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    //alias(libs.plugins.android.application) apply false
    //alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktorfit) apply false

    alias(libs.plugins.multiplatform).apply(false)
    alias(libs.plugins.compose).apply(false)
    alias(libs.plugins.cocoapods).apply(false)
    alias(libs.plugins.android.application).apply(false)
    // alias(libs.plugins.libres).apply(false)
    // alias(libs.plugins.buildConfig).apply(false)
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = libs.versions.java.get()
    }
}
