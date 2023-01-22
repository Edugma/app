plugins {
    id("android-domain-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jvm)
    api(libs.kotlinx.serializationJson)
    api(libs.kotlinx.serializationCbor)
    api(libs.koin.core)
    api(libs.ktorUtils)
    api(libs.kotlinx.dateTime)
}
