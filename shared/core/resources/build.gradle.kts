plugins {
    id("mp-compose-lib")
    id("mp-resource-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
    }
}

android {
    namespace = "io.edugma.core.resources"
}