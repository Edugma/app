plugins {
    id("android-data-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(projects.data.base)
    api(projects.domain.schedule)
}
android {
    namespace = "io.edugma.data.schedule"
}
