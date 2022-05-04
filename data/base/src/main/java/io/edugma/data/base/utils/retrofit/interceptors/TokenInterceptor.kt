package io.edugma.data.base.utils.retrofit.interceptors

import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.get
import io.edugma.domain.base.PrefKeys
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val preferences: PreferencesDS
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { preferences.get(PrefKeys.TokenKey, "") }
        return if (token.isEmpty()) {
            chain
                .request()
                .let { chain.proceed(it) }
        } else {
            chain
                .request()
                .newBuilder()
                .addHeader(PrefKeys.TokenKey,  token)
                .build()
                .let { chain.proceed(it) }
        }
    }
}