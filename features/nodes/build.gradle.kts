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

                implementation(projects.core.navigation)
                implementation(projects.core.arch)
                implementation(projects.core.utils)
                implementation(libs.koin.core)

                implementation(projects.data.base)
                implementation(libs.ktorfit)
            }
        }
    }
}

configure<de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration> {
    version = libs.versions.ktorfit.get()
}

dependencies {
    kspAllPlatforms(libs.ktorfit.ksp)
}

android {
    namespace = "io.edugma.features.nodes"
}
