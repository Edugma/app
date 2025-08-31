import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.PluginDependenciesSpecScope


//fun PluginDependenciesSpecScope.edugma(pluginName: String) {
//    id("com.edugma.$pluginName")
//}
//
//fun PluginDependenciesSpecScope.edugmaAndroidLib() {
//    edugma("android-lib")
//}

fun LibrariesForLibs.composeRuntime(): String {
    val version = versions.composeMultiplatform.get()

    return "org.jetbrains.compose.runtime:runtime:$version"
}
