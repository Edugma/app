plugins {
    id("compose-android-lib")
}

android {
    namespace = "io.edugma.core.navigation"
    resourcePrefix("core_nav_")
}

dependencies {
    implementation(projects.core.designSystem)
    api(projects.navigation.core)

    implementation(libs.kotlinx.dateTime)
}
