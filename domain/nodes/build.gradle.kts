plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
}


kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.domain.baseDomain)
            }
        }
    }
}

android.namespace = "io.edugma.domain.nodes"
