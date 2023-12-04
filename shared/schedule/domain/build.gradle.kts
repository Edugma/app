plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
}


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared.core.api)
            }
        }
    }
}

android.namespace = "io.edugma.domain.schedule"
