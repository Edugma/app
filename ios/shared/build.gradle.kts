plugins {
    id("mp-ios-lib")
    id("org.jetbrains.kotlin.native.cocoapods")
}

version = "1.0.0"

kotlin {
    task("testClasses")
    sourceSets {
        commonMain {
            dependencies {
                api(projects.shared.app)
            }
        }
//        iosMain {
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
        podfile = project.file("../app/Podfile")
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

//android {
//    namespace = "io.edugma.ios.shared"
//}
