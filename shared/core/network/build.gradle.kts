plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
}

configure<de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration> {
    version = libs.versions.ktorfit.get()
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.core.api)

                implementation(libs.ktorfit)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.contentNegotiation)
                implementation(libs.ktor.client.serialization.json)
                api(libs.paging)
                api(libs.paging.compose)

                api(libs.androidx.datastore)
                implementation(libs.kermit)
            }
        }
    }
}

dependencies {
    kspAllPlatforms(libs.ktorfit.ksp)
}

android.namespace = "io.edugma.core.network"

