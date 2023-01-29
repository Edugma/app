plugins {
    id("compose-android-lib")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.features.base.core)
    implementation(projects.features.base.navigation)
    implementation(projects.features.base.elements)
    api(projects.features.schedule.domain)
}
android {
    namespace = "io.edugma.features.schedule.menu"
}
