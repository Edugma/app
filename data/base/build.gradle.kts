plugins {
    id("android-data-base")
    kotlin("plugin.serialization")
}

dependencies {
    api(project(Modules.Domain.Base))

    api(Libs.Networking.retrofit)
    api(Libs.Networking.okHttp)
    api(Libs.Networking.okHttpLogging)
    api(Libs.Networking.paging)
    api(Libs.Networking.pagingCompose)

    debugApi(Libs.Storage.KodeinDB.debug)
    releaseApi(Libs.Storage.KodeinDB.release)
    api(Libs.Storage.KodeinDB.kotlinxSerializer)
}