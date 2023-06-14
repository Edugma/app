plugins {
    id("compose-android-lib")
}

android {
    namespace = "io.edugma.core.ui"
    resourcePrefix("core_ui_")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(libs.kotlinx.serializationJson)
}
