plugins {
    kotlin("android")
    id("com.edugma.android-lib")
}

android {
    namespace = "com.edugma.core.androidResources"
    resourcePrefix("core_ed_")
}

dependencies {
    implementation(libs.material3)
}

