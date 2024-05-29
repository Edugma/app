plugins {
    id("mp-feature-lib")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core.designSystem)
                implementation(projects.shared.core.ui)

                implementation(projects.shared.core.navigation)
            }
        }
    }
}

android {
    namespace = "com.edugma.features.home"
}
