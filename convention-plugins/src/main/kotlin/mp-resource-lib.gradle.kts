@file:Suppress("UNUSED_VARIABLE")
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(compose.components.resources)
        }
    }
}

compose.resources {
    publicResClass = true
    //packageOfResClass = "me.sample.library.resources"
    generateResClass = auto
}

//multiplatformResources {
//    val regex = """-(.)""".toRegex()
//    val projectName = project.path
//        .replace('\\', '.') //for windows
//        .replace('/', '.') //for mac
//        .replace(':', '.')
//
//    var resPath = projectName
//    regex.findAll(path).forEach { match ->
//        resPath = resPath.replace("-${match.groups[1]!!.value}", match.groups[1]!!.value.uppercase())
//    }
//
//    if (!resPath.startsWith(".")) {
//        resPath = ".$resPath"
//    }
//
//    resPath = resPath.replace("shared", "")
//        .replace("libs", "")
//        .replace("..", ".")
//
//
//    multiplatformResourcesPackage = "com.edugma${resPath}"
//}
