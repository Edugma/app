plugins {
    id("mp-common-lib")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core.api)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.appCompat)
            }
        }
    }
}

kotlin.android {
    namespace = "com.edugma.core.system"
    //resourcePrefix("core_system_")
}
