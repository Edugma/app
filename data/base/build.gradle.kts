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
    api(projects.domain.base)

    ksp(libs.ktorfit.ksp)
    implementation(libs.ktorfit)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.serialization.json)
    api(libs.paging)
    api(libs.paging.compose)

    debugApi(libs.kodeindb.debug)
    releaseApi(libs.kodeindb.release)
    api(libs.kodeindb.kotlinx.serializer)
    api(libs.androidx.datastore)
}
android {
    namespace = "io.edugma.data.base"
}
