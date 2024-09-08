plugins {
    id("mp-common-lib")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core.api)
                implementation(projects.shared.core.network)

                implementation(libs.ktorfit)
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

dependencies {
    kspAllPlatforms(libs.ktorfit.ksp)
}

android.namespace = "com.edugma.core.storage"
