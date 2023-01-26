plugins {
    id("android-feature-base")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.features.base.core)
    implementation(projects.features.base.navigation)
    implementation(projects.features.base.elements)
}
android {
    namespace = "io.edugma.features.misc.settings"
}
