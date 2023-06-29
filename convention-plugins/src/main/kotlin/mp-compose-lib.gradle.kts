import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("mp-common-lib")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                //implementation(platform(libs.compose.bom))
                //implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.moko.resources)
                implementation(libs.moko.resourcesCompose)
                implementation(libs.insetsx)
                //implementation(compose.preview)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                // debugImplementation
                implementation(libs.compose.uiTooling)
                implementation(libs.compose.uiToolingPreview)
//                implementation(libs.androidx.appcompat)
//                implementation(libs.androidx.activityCompose)
//                implementation(libs.compose.uitooling)
//                implementation(libs.kotlinx.coroutines.android)
            }
        }

        val iosMain by getting {
            dependencies {
            }
        }
    }
}

multiplatformResources {
    val regex = """-(.)""".toRegex()
    val projectName = project.path
        .replace('\\', '.') //for windows
        .replace('/', '.') //for mac
        .replace(':', '.')

    var resPath = projectName
    regex.findAll(path).forEach { match ->
        resPath = resPath.replace("-${match.groups[1]!!.value}", match.groups[1]!!.value.uppercase())

    }

    multiplatformResourcesPackage = "io.edugma.${resPath}.resources"
}
