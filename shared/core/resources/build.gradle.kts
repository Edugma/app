plugins {
    id("mp-compose-lib")
    id("mp-resource-lib")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
            }
        }
    }
}

kotlin.android {
    namespace = "com.edugma.core.resources"
}
