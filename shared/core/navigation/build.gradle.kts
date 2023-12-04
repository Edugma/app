plugins {
    id("mp-compose-lib")
    alias(libs.plugins.kotlinx.atomic)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core.designSystem)
                implementation(projects.shared.core.api)
                api(projects.libs.navigation.core)

                implementation(libs.kotlinx.dateTime)
            }
        }
    }
}

android {
    namespace = "io.edugma.core.navigation"
    resourcePrefix("core_nav_")
}
