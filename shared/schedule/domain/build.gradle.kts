plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
}


kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.core.api)
            }
        }
    }
}

android.namespace = "io.edugma.domain.schedule"