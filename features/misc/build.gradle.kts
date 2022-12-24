plugins {
    id("android-feature-base")
}

dependencies {
    implementation(project(Modules.Features.Base.Core))
    implementation(project(Modules.Features.Base.Navigation))
    implementation(project(Modules.Features.Base.Elements))
}
android {
    namespace = "io.edugma.features.misc"
}
