plugins {
    id("compose-android-lib")
}

android {
    namespace = "io.edugma.features.schedule.sources"
    resourcePrefix("schedule_sources_")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.ui)

    implementation(projects.features.base.core)
    implementation(projects.features.base.navigation)
    implementation(projects.features.base.elements)
    api(projects.features.schedule.domain)
}
