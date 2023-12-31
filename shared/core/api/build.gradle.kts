plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlinx.atomic)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.uuid)
                implementation(libs.ktor.client.core)
                implementation(libs.kotlinx.atomic)
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

android.namespace = "io.edugma.core.api"
