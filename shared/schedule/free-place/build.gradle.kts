plugins {
    id("mp-feature-lib")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core.designSystem)
                implementation(projects.shared.core.ui)
                implementation(projects.shared.core.utils)

                implementation(projects.shared.core.navigation)
                api(projects.shared.schedule.domain)
            }
        }
    }
}

android {
    namespace = "io.edugma.features.schedule.free_place"
}
