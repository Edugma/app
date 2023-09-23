import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.PluginDependenciesSpecScope

fun DependencyHandlerScope.kspAllPlatforms(dependencyNotation: Any) {
    add("kspCommonMainMetadata", dependencyNotation)
    add("kspAndroid", dependencyNotation)
    add("kspIosX64", dependencyNotation)
    add("kspIosArm64", dependencyNotation)
    add("kspIosSimulatorArm64", dependencyNotation)
}

//fun PluginDependenciesSpecScope.edugma(pluginName: String) {
//    id("io.edugma.$pluginName")
//}
//
//fun PluginDependenciesSpecScope.edugmaAndroidLib() {
//    edugma("android-lib")
//}
