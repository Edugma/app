plugins {
    id("compose-android-lib")
}

dependencies {
    api(projects.features.base.core)
    implementation(projects.core.designSystem)

    implementation(projects.features.schedule.domain)
}
android {
    namespace = "io.edugma.features.navigation"
}
