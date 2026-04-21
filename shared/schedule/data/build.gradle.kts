plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared.core.storage)
                api(projects.shared.schedule.domain)
            }
        }
    }
}

kotlin.android.namespace = "com.edugma.data.schedule"
