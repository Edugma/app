plugins {
    id("feature-lib")
}

dependencies {
    implementation(projects.core.androidResources)
    implementation(projects.core.navigation)
    api(projects.features.schedule.domain)

    implementation(libs.androidx.appWidget)
    implementation(libs.material3)
}
android {
    namespace = "io.edugma.features.schedule.appwidget"
}
