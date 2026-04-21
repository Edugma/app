plugins {
    id("mp-feature-lib")
    alias(libs.plugins.kotlin.serialization)
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
            }
        }
    }
}

kotlin.android {
    namespace = "com.edugma.features.account"
}
