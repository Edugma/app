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
    implementation(project(":android:resources"))
    implementation(project(":shared:core:navigation"))
    implementation(projects.shared.core.api)
    //implementation(projects.android.schedule.appwidget)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.activity)

    implementation(libs.koin.android)
    implementation(libs.androidx.splashScreen)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.viewmodel.compose)
    implementation(libs.essenty.instanceKeeper)
    //implementation("androidx.compose.runtime:runtime-tracing:1.0.0-alpha03")

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.junit.ext)
    androidTestImplementation(libs.test.espressoCore)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.uiTest)
}
