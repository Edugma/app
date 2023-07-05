plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.core.designSystem)
                implementation(projects.shared.core.navigation)
                implementation(projects.shared.core.arch)

                implementation(projects.shared.nodes)

                implementation(projects.shared.home)

                implementation(projects.shared.schedule.elements)
                implementation(projects.shared.schedule.menu)
                implementation(projects.shared.schedule.daily)
                implementation(projects.shared.schedule.scheduleInfo)
                implementation(projects.shared.schedule.calendar)
                implementation(projects.shared.schedule.lessonsReview)
                implementation(projects.shared.schedule.sources)
                implementation(projects.shared.schedule.history)
                implementation(projects.shared.schedule.freePlace)

                implementation(projects.shared.account)

                implementation(projects.shared.misc.menu)
                implementation(projects.shared.misc.settings)


                implementation(projects.shared.schedule.data)
            }
        }
    }
}

android {
    namespace = "io.edugma.features.app"
}
