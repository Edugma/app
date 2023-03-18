plugins {
    id("compose-android-lib")
}

android {
    namespace = "io.edugma.features.schedule.scheduleInfo"
    resourcePrefix("schedule_info_")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.ui)
    implementation(projects.core.utils)

    implementation(projects.features.base.core)
    implementation(projects.features.base.navigation)
    implementation(projects.features.base.elements)
    api(projects.features.schedule.domain)
    implementation(projects.features.schedule.elements)
}
