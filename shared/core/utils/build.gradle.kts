plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core.arch)
                implementation(projects.shared.core.api)

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kermit)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
            }
        }
    }
}

android {
    namespace = "com.edugma.core.utils"
    resourcePrefix("core_utils_")
}
