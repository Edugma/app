plugins {
    id("mp-feature-lib")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core.designSystem)
                implementation(projects.shared.core.ui)

                implementation(projects.shared.core.navigation)
                implementation(projects.shared.misc.settings)
                implementation(projects.shared.misc.aboutApp)
            }
        }
    }
}

kotlin.android {
    namespace = "com.edugma.features.misc.menu"
}
