@file:Suppress("UNUSED_VARIABLE")
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("dev.icerock.mobile.multiplatform-resources")
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

    if (!resPath.startsWith(".")) {
        resPath = ".$resPath"
    }

    resPath = resPath.replace("shared", "")
        .replace("libs", "")
        .replace("..", ".")


    multiplatformResourcesPackage = "io.edugma${resPath}"
}
