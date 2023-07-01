plugins {
    id("mp-feature-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.designSystem)
                implementation(projects.core.ui)
                implementation(projects.core.utils)

                implementation(projects.core.navigation)
                api(projects.features.schedule.domain)
            }
        }
    }
}

android {
    namespace = "io.edugma.features.schedule.elements"
}
