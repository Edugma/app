plugins {
    id("android-feature-base")
}

dependencies {
    api(projects.features.base.core)

    implementation(projects.domain.schedule)
}
android {
    namespace = "io.edugma.features.navigation"
}
