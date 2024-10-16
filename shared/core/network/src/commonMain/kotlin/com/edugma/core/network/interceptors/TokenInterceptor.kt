package com.edugma.core.network.interceptors

import com.edugma.core.api.repository.AuthInterceptorRepository
import com.edugma.core.network.KtorInterceptor
import com.edugma.core.network.SecurityAttribute
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

class TokenInterceptor(
    private val authInterceptorRepository: AuthInterceptorRepository,
) : KtorInterceptor {

    override suspend fun invoke(sender: Sender, request: HttpRequestBuilder): HttpClientCall {
        if (request.attributes[SecurityAttribute] != true) return sender.execute(request)
        // TODO: Переделать

        val accessToken = authInterceptorRepository.getAccessToken()
        if (!accessToken.isNullOrEmpty()) {
            if (accessToken.startsWith("Bearer ")) {
                request.header(AuthHeader, accessToken)
            } else {
                request.header(AuthHeader, "Bearer $accessToken")
            }
        }

        val accountId = authInterceptorRepository.getAccountId()
        if (!accountId.isNullOrEmpty()) {
            request.header(EdugmaAccountIdHeader, accountId)
        }
        return sender.execute(request)
    }

    companion object {
        private const val AuthHeader = "Authorization"
        private const val EdugmaAccountIdHeader = "X-Edugma-Account-Id"
    }
}
