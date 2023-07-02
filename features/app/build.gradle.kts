plugins {
    id("mp-compose-lib")
    id("org.jetbrains.kotlin.native.cocoapods")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.designSystem)
                implementation(projects.core.navigation)
                implementation(projects.core.arch)

                implementation(projects.features.nodes)

                implementation(projects.features.home)

                implementation(projects.features.schedule.elements)
                implementation(projects.features.schedule.menu)
                implementation(projects.features.schedule.daily)
                implementation(projects.features.schedule.scheduleInfo)
                implementation(projects.features.schedule.calendar)
                implementation(projects.features.schedule.lessonsReview)
                implementation(projects.features.schedule.sources)
                implementation(projects.features.schedule.history)
                implementation(projects.features.schedule.freePlace)

                implementation(projects.features.account)

                implementation(projects.features.misc.menu)
                implementation(projects.features.misc.settings)


                implementation(projects.data.nodes)
                implementation(projects.features.schedule.data)
                implementation(projects.data.account)
            }
        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "Compose application framework"
        homepage = "empty"
        ios.deploymentTarget = "15.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "sharedApp"
            isStatic = true
        }
        extraSpecAttributes["resources"] =
            "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }
}

android {
    namespace = "io.edugma.features.app"
}
