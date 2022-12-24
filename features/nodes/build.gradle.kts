plugins {
    id("android-feature-base")
}

dependencies {
    implementation(project(Modules.Features.Base.Core))
    implementation(project(Modules.Features.Base.Navigation))
    implementation(project(Modules.Features.Base.Elements))

    api(project(Modules.Domain.Nodes))
}
android {
    namespace = "io.edugma.features.nodes"
}
