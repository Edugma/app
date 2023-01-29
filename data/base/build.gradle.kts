plugins {
    id("android-lib")
    kotlin("plugin.serialization")
}

dependencies {
    api(projects.domain.base)

    api(libs.retrofit)
    api(libs.okHttp)
    api(libs.okHttp.logging)
    api(libs.paging)
    api(libs.paging.compose)

    debugApi(libs.kodeindb.debug)
    releaseApi(libs.kodeindb.release)
    api(libs.kodeindb.kotlinx.serializer)
}
android {
    namespace = "io.edugma.data.base"
}
