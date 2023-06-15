plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.compose)
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.ktlint)
    //implementation(libs.gradlePlugin.ksp)

    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
