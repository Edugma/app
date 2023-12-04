plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core.designSystem)
                implementation(projects.shared.core.arch)
                implementation(libs.kotlinx.serializationJson)
            }
        }
    }
}

android {
    namespace = "io.edugma.core.ui"
    resourcePrefix("core_ui_")
}
