plugins {
    id("compose-android-lib")
}

android {
    namespace = "io.edugma.navigation.core"
}

dependencies {
    implementation(libs.compose.activity)
}
