plugins {
    id("android-feature-base")
}

dependencies {
    implementation(projects.features.base.core)
    implementation(projects.features.base.navigation)
    implementation(projects.features.base.elements)

    implementation(projects.features.misc.settings)
}
android {
    namespace = "io.edugma.features.misc.menu"
}
