plugins {
    id("android-lib")
    kotlin("plugin.serialization")
}

dependencies {
    api(projects.data.base)
    api(projects.features.schedule.domain)
}
android {
    namespace = "io.edugma.data.schedule"
}
