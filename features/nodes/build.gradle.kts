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
                implementation(projects.features.base.elements)
                implementation(projects.core.arch)
                implementation(projects.core.utils)
                implementation(libs.koin.core)

                api(projects.domain.nodes)
            }
        }
    }
}

android {
    namespace = "io.edugma.features.nodes"
}
