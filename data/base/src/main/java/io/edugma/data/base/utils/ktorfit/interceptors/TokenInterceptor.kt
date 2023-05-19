package io.edugma.data.base.utils.ktorfit.interceptors

import io.edugma.data.base.consts.CacheConst.TokenKey
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.getSourceValue
import io.edugma.data.base.utils.ktorfit.KtorInterceptor
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

class TokenInterceptor(
    private val preferences: PreferencesDS,
) : KtorInterceptor {

    companion object {
        const val AuthHeader = "Authorization"
    }

    override suspend fun invoke(sender: Sender, request: HttpRequestBuilder): HttpClientCall {
        // TODO: Переделать

        val token = preferences.getSourceValue(TokenKey)
        return if (token.isNullOrEmpty()) {
            sender.execute(request)
        } else {
            request.header(AuthHeader, token)

            sender.execute(request)
        }
    }
}
