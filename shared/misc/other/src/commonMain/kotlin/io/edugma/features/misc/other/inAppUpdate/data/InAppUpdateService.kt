package io.edugma.features.misc.other.inAppUpdate.data

import de.jensklingenberg.ktorfit.http.GET

interface InAppUpdateService {
    @GET("versions/android/last-version.json")
    suspend fun getLastVersion(): Result<Version>
}
