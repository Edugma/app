plugins {
    id("android-data-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(projects.data.base)
    api(projects.domain.account)
}
android {
    namespace = "io.edugma.data.account"
}
