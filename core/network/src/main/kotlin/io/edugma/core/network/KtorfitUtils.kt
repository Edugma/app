package io.edugma.core.network

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.Sender
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun buildKtorfit(client: HttpClient, baseUrl: String = ""): Ktorfit {
    return ktorfit {
        baseUrl(baseUrl)
        httpClient(client)
        responseConverter(ResultConverter())
    }
}

fun buildKtorClient(
    interceptors: List<KtorInterceptor>,
): HttpClient {
    val client = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 60_000
            connectTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    allowStructuredMapKeys = true
                },
            )
            json(
                Json {
                    ignoreUnknownKeys = true
                    allowStructuredMapKeys = true
                },
                ContentType.Text.Any,
            )
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.d(message, tag = "Network")
                }
            }
            level = LogLevel.ALL
        }
    }
    client.plugin(HttpSend).apply {
        intercept { request ->
            execute(request)
        }
        interceptors.forEach {
            intercept(it)
        }
    }

    return client
}

typealias KtorInterceptor = suspend (sender: Sender, request: HttpRequestBuilder) -> HttpClientCall
