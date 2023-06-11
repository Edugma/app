plugins {
    id("compose-android-lib")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.ui)

    implementation(projects.features.base.core)
    implementation(projects.core.navigation)
    implementation(projects.features.base.elements)
}
android {
    namespace = "io.edugma.features.home"
}
