plugins {
    id("android-lib")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
}

configure<de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration> {
    version = libs.versions.ktorfit.get()
}

dependencies {
    api(projects.data.base)
    api(projects.features.schedule.domain)

    ksp(libs.ktorfit.ksp)
    implementation(libs.ktorfit)
}
android {
    namespace = "io.edugma.data.schedule"
}
