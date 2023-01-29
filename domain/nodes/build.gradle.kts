plugins {
    id("kotlin-lib")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(projects.domain.base)
}
