plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.designSystem)
                implementation(libs.kotlinx.serializationJson)
            }
        }
    }
}

android {
    namespace = "io.edugma.core.ui"
    resourcePrefix("core_ui_")
}
