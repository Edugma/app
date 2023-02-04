plugins {
    id("compose-android-lib")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.ui)

    implementation(projects.features.base.core)
    implementation(projects.features.base.navigation)
    implementation(projects.features.base.elements)

    implementation(projects.features.misc.settings)
}
android {
    namespace = "io.edugma.features.misc.menu"
}
