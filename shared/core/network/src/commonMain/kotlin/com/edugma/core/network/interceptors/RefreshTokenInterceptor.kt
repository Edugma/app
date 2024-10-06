package com.edugma.core.network.interceptors

import com.edugma.core.api.repository.SettingsRepository
import com.edugma.core.network.KtorInterceptor
import com.edugma.core.network.SecurityAttribute
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

class RefreshTokenInterceptor(
    private val settingsRepository: SettingsRepository,
) : KtorInterceptor {

    companion object {
        const val AuthHeader = "Authorization"
        const val TokenKey = "token"
    }

    override suspend fun invoke(sender: Sender, request: HttpRequestBuilder): HttpClientCall {
        if (request.attributes[SecurityAttribute] != true) return sender.execute(request)
        // TODO: Переделать

        val token = settingsRepository.getString(TokenKey)
        return if (token.isNullOrEmpty()) {
            sender.execute(request)
        } else {
            request.header(AuthHeader, token)

            sender.execute(request)
        }
    }
}
