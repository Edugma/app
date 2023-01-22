plugins {
    id("android-feature-base")
}

dependencies {
    api(projects.features.base.core)
}
android {
    namespace = "io.edugma.features.elements"
}
