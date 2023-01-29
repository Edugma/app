plugins {
    id("android-lib")
    kotlin("plugin.serialization")
}

dependencies {
    api(projects.data.base)
    api(projects.domain.account)
}
android {
    namespace = "io.edugma.data.account"
}
