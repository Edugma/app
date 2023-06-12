plugins {
    id("compose-android-lib")
}

dependencies {
    api(projects.domain.base)
    api(projects.data.base)
    api(projects.navigation.core)
    api(projects.core.arch)

    api(platform(libs.compose.bom))
    api(libs.compose.activity)
    api(libs.compose.material)

    api(libs.lottie)
    api(libs.fluentIcons) { artifact { type = "aar" } }

    api(libs.material3)

    api(libs.koin.compose)

    api(libs.androidx.startup)
    implementation(libs.kermit)
}
android {
    namespace = "io.edugma.features.base.core"
}
