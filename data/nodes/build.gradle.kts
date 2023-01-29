plugins {
    id("android-lib")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(projects.data.base)
    api(projects.domain.nodes)
}
android {
    namespace = "io.edugma.data.nodes"
}
