plugins {
    id("compose-android-lib")
}

android {
    namespace = "io.edugma.core.utils"
    resourcePrefix("core_utils_")
}

dependencies {
    implementation(libs.androidx.coreKtx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.arch)
    implementation(libs.kermit)
}
