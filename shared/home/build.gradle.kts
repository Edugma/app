plugins {
    id("mp-feature-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.core.designSystem)
                implementation(projects.shared.core.ui)

                implementation(projects.shared.core.navigation)
            }
        }
    }
}

android {
    namespace = "io.edugma.features.home"
}
