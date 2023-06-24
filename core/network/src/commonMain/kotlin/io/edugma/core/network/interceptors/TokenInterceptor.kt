package io.edugma.core.network.interceptors

import io.edugma.core.network.KtorInterceptor
import io.edugma.domain.base.repository.SettingsRepository
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

class TokenInterceptor(
    private val settingsRepository: SettingsRepository,
) : KtorInterceptor {

    companion object {
        const val AuthHeader = "Authorization"
        const val TokenKey = "token"
    }

    override suspend fun invoke(sender: Sender, request: HttpRequestBuilder): HttpClientCall {
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
