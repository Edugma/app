plugins {
    id("android-domain-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(project(Modules.Domain.Base))
}
android {
    namespace = "io.edugma.domain.schedule"
}
