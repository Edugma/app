plugins {
    id("android-data-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(project(Modules.Data.Base))
    api(project(Modules.Domain.Nodes))
}
android {
    namespace = "io.edugma.data.nodes"
}
