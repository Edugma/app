plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.serializationJson)
                api(libs.kotlinx.serializationCbor)
                api(libs.koin.core)
                api(libs.ktorUtils)
                api(libs.kotlinx.dateTime)
            }
        }
    }
}

android.namespace = "io.edugma.domain.base"
