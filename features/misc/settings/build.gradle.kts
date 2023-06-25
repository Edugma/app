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
                implementation(projects.features.base.elements)
            }
        }
    }
}

android {
    namespace = "io.edugma.features.misc.settings"
}
