plugins {
    id("compose-android-lib")
}

dependencies {
    implementation(projects.features.base.core)
    implementation(projects.features.base.navigation)
    implementation(projects.features.base.elements)
    api(projects.features.schedule.domain)

    implementation(libs.androidx.appWidget)
}
android {
    namespace = "io.edugma.features.schedule.appwidget"
}
