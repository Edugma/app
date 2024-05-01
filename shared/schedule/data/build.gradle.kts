plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
}

configurations.all {
    resolutionStrategy.eachDependency {
        val requestedGroup = this.requested.group
        if (requestedGroup == "org.jetbrains.kotlin") {
            useVersion(libs.versions.kotlin.get())
        }
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared.core.storage)
                api(projects.shared.schedule.domain)

                implementation(libs.ktorfit)
            }
        }
    }
}

dependencies {
    kspAllPlatforms(libs.ktorfit.ksp)
}

android.namespace = "io.edugma.data.schedule"
