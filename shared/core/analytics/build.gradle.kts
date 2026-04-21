plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.tracer.crash)
        }
        commonMain.dependencies {
            implementation(projects.shared.core.api)
        }
    }
}

kotlin.android.namespace = "com.edugma.core.analytics"
