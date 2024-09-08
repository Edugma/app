plugins {
    `kotlin-dsl`
}

//repositories {
//    gradlePluginPortal()
//    mavenCentral()
//    google()
//    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
//}

dependencies {
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.composePlugin)
    implementation(libs.gradlePlugin.composeCompiler)
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.ktlint)
    implementation(libs.gradlePlugin.detekt)

    //implementation(libs.gradlePlugin.ksp)

    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

//@file:Suppress("UnstableApiUsage")
//dependencyResolutionManagement {
//    repositories {
//        gradlePluginPortal()
//        mavenCentral()
//        google()
//        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
//    }
//    versionCatalogs {
//        create("libs") {
//            from(files("../gradle/libs.versions.toml"))
//        }
//    }
//}
//
//rootProject.name = "convention-plugins"
