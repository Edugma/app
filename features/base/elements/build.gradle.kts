plugins {
    id("android-feature-base")
}

dependencies {
    api(project(Modules.Features.Base.Core))
}
android {
    namespace = "io.edugma.features.elements"
}
