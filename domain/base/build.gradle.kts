plugins {
    id("kotlin-lib")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.serializationJson)
    api(libs.kotlinx.serializationCbor)
    api(libs.koin.core)
    api(libs.ktorUtils)
    api(libs.kotlinx.dateTime)
}
