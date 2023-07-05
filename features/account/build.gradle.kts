plugins {
    id("mp-feature-lib")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.designSystem)
                implementation(projects.core.ui)
                implementation(projects.core.utils)
                implementation(projects.core.api)
                implementation(projects.core.navigation)
                implementation(libs.paging)
                implementation(libs.paging.compose)

                api(projects.data.base)

                implementation(libs.ktorfit)
            }
        }
    }
}

dependencies {
    kspAllPlatforms(libs.ktorfit.ksp)
}


configure<de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration> {
    version = libs.versions.ktorfit.get()
}

android {
    namespace = "io.edugma.features.account"
}
