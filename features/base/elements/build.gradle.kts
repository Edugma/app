plugins {
    id("mp-compose-lib")
    alias(libs.plugins.kotlinx.atomic)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.designSystem)
                implementation(projects.domain.base)
                implementation(libs.kotlinx.atomic)
            }
        }
    }
}

android {
    namespace = "io.edugma.features.elements"
}

