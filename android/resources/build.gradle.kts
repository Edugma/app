plugins {
    id("com.edugma.android-lib")
}

kotlin.android {
    namespace = "com.edugma.core.androidResources"
    androidResources {
        enable = true
        //resourcePrefix = "core_ed_"
    }
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.material3)
            }
        }
    }
}

