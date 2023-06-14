plugins {
    id("feature-lib")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.ui)

    implementation(projects.core.navigation)
    implementation(projects.features.base.elements)

    implementation(projects.features.misc.settings)
}
android {
    namespace = "io.edugma.features.misc.menu"
}
