plugins {
    id("android-feature-base")
}

dependencies {
    api(projects.features.base.core)
    implementation(projects.core.designSystem)
}
android {
    namespace = "io.edugma.features.elements"
}
