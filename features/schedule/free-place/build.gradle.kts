plugins {
    id("feature-lib")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.ui)
    implementation(projects.core.utils)

    implementation(projects.core.navigation)
    implementation(projects.features.base.elements)
    api(projects.features.schedule.domain)
    implementation(libs.compose.material)
}
android {
    namespace = "io.edugma.features.schedule.free_place"
}
