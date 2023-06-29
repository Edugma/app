plugins {
    id("compose-android-lib")
}

android {
    namespace = "io.edugma.core.androidResources"
    resourcePrefix("core_ed_")
}

dependencies {
    implementation(projects.domain.base)
    implementation(projects.data.base)

    implementation(libs.androidx.lifecycleRuntime)

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appCompat)
    implementation(libs.lottie)
    implementation(libs.imageLoader)
    implementation(libs.compose.material)
    implementation(libs.compose.uiUtil)

    api(libs.fluentIcons) { artifact { type = "aar" } }
}

