plugins {
    id("feature-lib")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.navigation)
    implementation(projects.features.base.elements)
}
android {
    namespace = "io.edugma.features.misc"
}
