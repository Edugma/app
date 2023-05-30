plugins {
    id("compose-android-lib")
    alias(libs.plugins.kotlinx.atomic)
}

dependencies {
    api(projects.features.base.core)
    implementation(projects.core.designSystem)
    implementation(libs.kotlinx.atomic)
}
android {
    namespace = "io.edugma.features.elements"
}
