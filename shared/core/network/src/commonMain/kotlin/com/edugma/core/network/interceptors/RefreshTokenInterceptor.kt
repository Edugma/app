package com.edugma.core.network.interceptors

import com.edugma.core.api.repository.AuthInterceptorRepository
import com.edugma.core.network.KtorInterceptor
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder

// TODO сделать
class RefreshTokenInterceptor(
    private val authInterceptorRepository: AuthInterceptorRepository,
) : KtorInterceptor {

    override suspend fun invoke(sender: Sender, request: HttpRequestBuilder): HttpClientCall {
        return sender.execute(request)
    }
}
