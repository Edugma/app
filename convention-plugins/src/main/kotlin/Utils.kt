import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.kspAllPlatforms(dependencyNotation: Any) {
    add("kspCommonMainMetadata", dependencyNotation)
    add("kspAndroid", dependencyNotation)
    add("kspIosX64", dependencyNotation)
    add("kspIosArm64", dependencyNotation)
    add("kspIosSimulatorArm64", dependencyNotation)
}
