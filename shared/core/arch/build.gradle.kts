plugins {
    id("mp-compose-lib")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared.core.api)
                api(projects.libs.navigation.core)
                api(projects.shared.core.navigation)
                api(libs.androidx.lifecycle.viewmodel)
                api(libs.androidx.lifecycle.runtime)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.kotlinx.dateTime)
            }
        }
    }
}

android.namespace = "com.edugma.core.arch"
