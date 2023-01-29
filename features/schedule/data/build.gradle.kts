plugins {
    id("android-lib")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(projects.data.base)
    api(projects.features.schedule.domain)
}
android {
    namespace = "io.edugma.data.schedule"
}
