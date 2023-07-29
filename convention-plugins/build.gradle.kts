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
    implementation(libs.gradlePlugin.compose)
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.mokoResources)
    implementation(libs.gradlePlugin.ktlint)

//    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
//    implementation("org.jetbrains.compose:compose-gradle-plugin:1.4.0")
//    implementation("com.android.tools.build:gradle:8.0.0")
//    implementation("dev.icerock.moko:resources-generator:0.23.0")
//    implementation("org.jmailen.gradle:kotlinter-gradle:3.14.0")


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
