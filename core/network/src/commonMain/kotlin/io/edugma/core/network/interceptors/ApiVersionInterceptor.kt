package io.edugma.core.network.interceptors

import io.edugma.core.network.KtorInterceptor
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

class ApiVersionInterceptor : KtorInterceptor {
    override suspend fun invoke(sender: Sender, request: HttpRequestBuilder): HttpClientCall {
        request.header("v", "1")

        return sender.execute(request)
    }
}
