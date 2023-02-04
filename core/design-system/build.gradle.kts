plugins {
    id("compose-android-lib")
}

android {
    namespace = "io.edugma.core.designSystem"
    resourcePrefix("core_ed_")
}

dependencies {
    implementation(projects.domain.base)
    implementation(projects.data.base)

    implementation(libs.androidx.lifecycleRuntime)

    implementation(libs.accompanist.systemUiController)
    implementation(libs.accompanist.flowLayout)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pagerIndicators)
    implementation(libs.accompanist.placeholder)
    implementation(libs.accompanist.swiperefresh)

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appCompat)
    implementation(libs.lottie)
    implementation(libs.coil)

    api(libs.fluentIcons) { artifact { type = "aar" } }
}

