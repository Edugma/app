plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.tracer.bom))
            implementation(libs.tracer.crash)
        }
        commonMain.dependencies {
            implementation(projects.shared.core.api)
        }
    }
}

kotlin.android.namespace = "com.edugma.core.analytics"
