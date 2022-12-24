plugins {
    id("android-domain-base")
    kotlin("plugin.serialization")
}

android {
    namespace = "io.edugma.domain.account"
}

dependencies {
    api(project(Modules.Domain.Base))
}
