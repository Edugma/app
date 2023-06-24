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
                api(projects.data.base)
                api(projects.features.schedule.domain)

                implementation(libs.ktorfit)
            }
        }
    }
}

dependencies {
    kspAllPlatforms(libs.ktorfit.ksp)
}

android.namespace = "io.edugma.data.schedule"
