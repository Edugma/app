plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
}


kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.domain.base)
            }
        }
    }
}

android.namespace = "io.edugma.domain.schedule"
