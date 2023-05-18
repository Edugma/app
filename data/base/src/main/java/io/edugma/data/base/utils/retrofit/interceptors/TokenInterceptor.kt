package io.edugma.data.base.utils.retrofit.interceptors

import io.edugma.data.base.consts.CacheConst.TokenKey
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.getSourceValue
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val preferences: PreferencesDS,
) : Interceptor {

    companion object {
        const val AuthHeader = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { preferences.getSourceValue(TokenKey) }
        return if (token.isNullOrEmpty()) {
            chain
                .request()
                .let { chain.proceed(it) }
        } else {
            chain
                .request()
                .newBuilder()
                .addHeader(AuthHeader, token)
                .build()
                .let { chain.proceed(it) }
        }
    }
}
