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
                implementation(projects.shared.core.designSystem)
                implementation(projects.shared.core.ui)
                implementation(projects.shared.core.utils)
                implementation(projects.shared.core.api)
                implementation(projects.shared.core.navigation)
                implementation(libs.paging)
                implementation(libs.paging.compose)

                api(projects.shared.core.storage)

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
