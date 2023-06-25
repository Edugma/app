plugins {
    id("mp-feature-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.designSystem)
                implementation(projects.core.ui)

                implementation(projects.core.navigation)
            }
        }
    }
}

android {
    namespace = "io.edugma.features.home"
}
