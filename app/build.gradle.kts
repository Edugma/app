plugins {
    id("android-app-base")
}

dependencies {
    implementation(project(Modules.Features.Base.Core))
    implementation(project(Modules.Features.Base.Navigation))
    implementation(project(Modules.Features.Base.Elements))

    implementation(project(Modules.Features.Nodes))
    implementation(project(Modules.Features.Home))
    implementation(project(Modules.Features.Schedule))
    implementation(project(Modules.Features.Schedule2.ScheduleInfo))
    implementation(project(Modules.Features.Account))

    implementation(project(Modules.Features.Misc.Menu))
    implementation(project(Modules.Features.Misc.Settings))


    implementation(project(Modules.Data.Nodes))
    implementation(project(Modules.Data.Schedule))
    implementation(project(Modules.Data.Account))

    testImplementation(Libs.Other.junit)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.espressoCore)
    androidTestImplementation(Libs.AndroidX.Compose.uiTest)
}
