plugins {
    id("compose-android-lib")
    alias(libs.plugins.kotlinx.atomic)
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.domain.base)
    implementation(libs.compose.material)
    implementation(libs.kotlinx.atomic)
}
android {
    namespace = "io.edugma.features.elements"
}
