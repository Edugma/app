plugins {
    id("android-domain-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(projects.domain.base)
}
