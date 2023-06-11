plugins {
    id("compose-android-lib")
}

android {
    namespace = "io.edugma.core.arch"
}

dependencies {
    api(projects.navigation.core)
    api(projects.core.navigation)

    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.kotlinx.dateTime)
}
