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
                implementation(projects.shared.core.api)

                implementation(projects.shared.core.navigation)
                implementation(libs.kotlinx.serializationJson)
                implementation(libs.ktor.client.core)
            }
        }
    }
}

kotlin.android {
    namespace = "com.edugma.features.misc.other"
}
