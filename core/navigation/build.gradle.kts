plugins {
    id("compose-android-lib")
}

android {
    namespace = "io.edugma.core.ui"
    resourcePrefix("core_ui_")
}

dependencies {
    implementation(projects.core.designSystem)
    api(projects.navigation.core)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    implementation(libs.kotlinx.dateTime)
}
