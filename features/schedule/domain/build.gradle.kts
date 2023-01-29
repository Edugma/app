plugins {
    id("kotlin-lib")
    kotlin("plugin.serialization")
}

dependencies {
    api(projects.domain.base)
}
