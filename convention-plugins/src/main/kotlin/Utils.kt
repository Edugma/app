import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.PluginDependenciesSpecScope

fun DependencyHandlerScope.kspAllPlatforms(dependencyNotation: Any) {
    add("kspCommonMainMetadata", dependencyNotation)
    add("kspAndroid", dependencyNotation)
    add("kspIosX64", dependencyNotation)
    add("kspIosArm64", dependencyNotation)
    add("kspIosSimulatorArm64", dependencyNotation)
    add("kspJs", dependencyNotation)
    //add("kspWasmJs", dependencyNotation)
}

//fun PluginDependenciesSpecScope.edugma(pluginName: String) {
//    id("io.edugma.$pluginName")
//}
//
//fun PluginDependenciesSpecScope.edugmaAndroidLib() {
//    edugma("android-lib")
//}

fun LibrariesForLibs.composeRuntime(): String {
    val version = versions.composeMultiplatform.get()

    return "org.jetbrains.compose.runtime:runtime:$version"
}
