plugins {
    id("compose-android-lib")
}

dependencies {
    api(projects.features.base.core)
    implementation(projects.core.designSystem)
    implementation(libs.accompanist.placeholder)
}
android {
    namespace = "io.edugma.features.elements"
}
