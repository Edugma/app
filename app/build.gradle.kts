plugins {
    id("android-app")
}

android {
    namespace = "io.edugma.android"
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.features.base.core)
    implementation(projects.features.base.navigation)
    implementation(projects.features.base.elements)

    implementation(projects.features.nodes)

    implementation(projects.features.home)

    implementation(projects.features.schedule.elements)
    implementation(projects.features.schedule.menu)
    implementation(projects.features.schedule.daily)
    implementation(projects.features.schedule.scheduleInfo)
    implementation(projects.features.schedule.calendar)
    implementation(projects.features.schedule.lessonsReview)
    implementation(projects.features.schedule.sources)
    implementation(projects.features.schedule.appwidget)
    implementation(projects.features.schedule.history)
    implementation(projects.features.schedule.freePlace)

    implementation(projects.features.account)

    implementation(projects.features.misc.menu)
    implementation(projects.features.misc.settings)


    implementation(projects.data.nodes)
    implementation(projects.features.schedule.data)
    implementation(projects.data.account)

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.junit.ext)
    androidTestImplementation(libs.test.espressoCore)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.uiTest)
}
