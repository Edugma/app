plugins {
    id("mp-feature-lib")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core.designSystem)
                implementation(projects.shared.core.ui)
                implementation(projects.shared.core.utils)
                implementation(projects.shared.core.api)
                implementation(projects.shared.core.navigation)

                api(projects.shared.core.storage)

                implementation(libs.ktorfit)
            }
        }
    }
}

dependencies {
    kspAllPlatforms(libs.ktorfit.ksp)
}

android {
    namespace = "com.edugma.features.account"
}
