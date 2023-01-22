plugins {
    id("android-feature-base")
}

dependencies {
    implementation(projects.features.base.core)
    implementation(projects.features.base.navigation)
    implementation(projects.features.base.elements)
    api(projects.domain.schedule)
}
android {
    namespace = "io.edugma.features.schedule.free_place"
}
