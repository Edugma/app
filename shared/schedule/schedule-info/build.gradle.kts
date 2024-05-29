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
                implementation(projects.shared.schedule.elements)
            }
        }
    }
}

android {
    namespace = "com.edugma.features.schedule.scheduleInfo"
    resourcePrefix("schedule_info_")
}
