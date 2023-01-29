plugins {
    id("compose-android-lib")
}

android.namespace = "io.edugma.core.designSystem"

dependencies {
    api(projects.domain.base)
    api(projects.data.base)

    api(platform(libs.compose.bom))
    api(libs.compose.ui)
    api(libs.compose.activity)
    api(libs.compose.material3)
    debugApi(libs.compose.uiTooling)
    api(libs.compose.uiToolingPreview)
    api(libs.androidx.lifecycleRuntime)
    api(libs.androidx.splashScreen)

    api(libs.accompanist.systemUiController)
    api(libs.accompanist.flowLayout)
    api(libs.accompanist.pager)
    api(libs.accompanist.pagerIndicators)
    api(libs.accompanist.placeholder)
    api(libs.accompanist.swiperefresh)
    api(libs.accompanist.permissions)

    api(libs.androidx.coreKtx)
    api(libs.androidx.appCompat)
    api(libs.androidx.constraintLayout)
    api(libs.lottie)
    api(libs.fluentIcons) { artifact { type = "aar" } }

    api(libs.material3)

    api(libs.koin.android)
    api(libs.koin.compose)

    api(libs.navigation)

    api(libs.coil)

    api(libs.androidx.startup)
}

