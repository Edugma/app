plugins {
    id("mp-compose-lib")
    id("org.jetbrains.kotlin.native.cocoapods")
}

version = "1.0.0"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.app)
            }
        }
//        val iosMain by getting {
//            dependencies {
//                api(projects.shared.core.icons)
//            }
//        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "Edugma shared code"
        homepage = "https://github.com/Edugma/app"
        ios.deploymentTarget = "15.5"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
            export(projects.shared.app)
            //embedBitcode(org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.BITCODE)
        }
        extraSpecAttributes["resources"] =
            "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }
}

android {
    namespace = "io.edugma.shared.ios"
}
