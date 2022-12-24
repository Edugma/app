plugins {
    id("android-data-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(project(Modules.Data.Base))
    api(projects.domain.schedule)
}
android {
    namespace = "io.edugma.data.schedule"
}
