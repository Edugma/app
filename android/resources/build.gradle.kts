plugins {
    kotlin("android")
    id("io.edugma.android-lib")
}

android {
    namespace = "io.edugma.core.androidResources"
    resourcePrefix("core_ed_")
}

dependencies {
    implementation(libs.material3)
}

