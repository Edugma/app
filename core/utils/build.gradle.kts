plugins {
    id("compose-android-lib")
}

android {
    namespace = "io.edugma.core.utils"
    resourcePrefix("core_utils_")
}

dependencies {
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycleViewModel)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jvm)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kermit)
}
