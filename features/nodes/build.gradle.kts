plugins {
    id("android-feature-base")
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.features.base.core)
    implementation(projects.features.base.navigation)
    implementation(projects.features.base.elements)

    api(projects.domain.nodes)
}
android {
    namespace = "io.edugma.features.nodes"
}
