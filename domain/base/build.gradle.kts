plugins {
    id("android-domain-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(Libs.KotlinX.Coroutines.core)
    api(Libs.KotlinX.Coroutines.jvm)
    api(Libs.KotlinX.serializationJson)
    api(Libs.KotlinX.serializationCbor)
    api(Libs.Di.koinCore)
    api(Libs.Other.ktorUtils)
    api(Libs.KotlinX.dateTime)
}