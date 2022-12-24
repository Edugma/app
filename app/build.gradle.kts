plugins {
    id("android-app-base")
}

dependencies {
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
    implementation(projects.data.schedule)
    implementation(projects.data.account)

    testImplementation(Libs.Other.junit)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.espressoCore)
    androidTestImplementation(Libs.AndroidX.Compose.uiTest)
}
android {
    namespace = "io.edugma.android"
}
