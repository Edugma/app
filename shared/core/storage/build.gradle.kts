plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core.api)
                implementation(projects.shared.core.network)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.contentNegotiation)
                implementation(libs.ktor.client.serialization.json)

                implementation(libs.kermit)
            }
        }

        val mobileCommonMain by getting {
            dependencies {
                implementation(libs.androidx.datastore)
            }
        }
    }
}

kotlin.android.namespace = "com.edugma.core.storage"
