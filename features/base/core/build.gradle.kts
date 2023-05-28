plugins {
    id("compose-android-lib")
}

dependencies {
    api(projects.domain.base)
    api(projects.data.base)

    api(platform(libs.compose.bom))
    api(libs.compose.activity)
    api(libs.compose.material)
    api(libs.androidx.splashScreen)


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
    implementation(libs.kermit)
}
android {
    namespace = "io.edugma.features.base.core"
}
