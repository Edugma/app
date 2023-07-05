plugins {
    id("android-app")
}

android {
    namespace = "io.edugma.android"

    packaging {
        resources.pickFirsts.add("MR/**")
    }
}

dependencies {
    implementation(project(":shared:app"))
    implementation(project(":shared:core:android-resources"))
    implementation(project(":shared:core:navigation"))
    //implementation(projects.shared.schedule.appwidget)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.activity)
    implementation(libs.compose.material3)
    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)

    implementation(libs.koin.android)
    implementation(libs.androidx.splashScreen)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.startup)

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.junit.ext)
    androidTestImplementation(libs.test.espressoCore)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.uiTest)
}
