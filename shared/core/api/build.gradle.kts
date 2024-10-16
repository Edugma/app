plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlinx.atomic)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.composeRuntime())
                implementation(libs.ktor.client.core)
                implementation(libs.kotlinx.atomic)
                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.serializationJson)
                api(libs.kotlinx.serializationCbor)

                api(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.core)

                api(libs.ktorUtils)
                api(libs.kotlinx.dateTime)
            }
        }

        val webCommonMain by getting {
            dependencies {
                implementation(npm("@js-joda/timezone", "2.3.0"))
            }
        }
    }
}

android.namespace = "com.edugma.core.api"
