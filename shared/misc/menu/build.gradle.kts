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
                implementation(projects.shared.misc.settings)
            }
        }
    }
}

android {
    namespace = "com.edugma.features.misc.menu"
}

