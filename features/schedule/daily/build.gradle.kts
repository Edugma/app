plugins {
    id("compose-android-lib")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.ui)
    implementation(projects.core.utils)

    implementation(projects.features.base.core)
    implementation(projects.core.navigation)
    implementation(projects.features.base.elements)
    api(projects.features.schedule.domain)
    api(projects.features.schedule.elements)
}
android {
    namespace = "io.edugma.features.schedule.daily"
}
