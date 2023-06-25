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
                implementation(projects.domain.account)
                implementation(libs.paging)
                implementation(libs.paging.compose)
            }
        }
    }
}

android {
    namespace = "io.edugma.features.account"
}
