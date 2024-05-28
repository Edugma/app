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
import io.ktor.util.AttributeKey
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

fun buildKtorfit(client: HttpClient, baseUrl: String = ""): Ktorfit {
    return ktorfit {
        if (baseUrl.isNotEmpty()) {
            baseUrl(baseUrl)
        }
        httpClient(client)
        converterFactories(ResultConverterFactory())
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun buildKtorClient(
    interceptors: List<KtorInterceptor>,
    isLogsEnabled: Boolean,
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
                    explicitNulls = false
                },
            )
            json(
                Json {
                    ignoreUnknownKeys = true
                    allowStructuredMapKeys = true
                    explicitNulls = false
                },
                ContentType.Text.Any,
            )
        }
        if (isLogsEnabled) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        co.touchlab.kermit.Logger.d(message, tag = "Network")
                    }
                }
                level = LogLevel.ALL
            }
        }
    }
    val httpSendPlugin = client.plugin(HttpSend)

    for (interceptor in interceptors) {
        httpSendPlugin.intercept { request ->
            interceptor.invoke(this, request)
        }
    }

    return client
}

// Not typealias due js crash "this.interceptor_1 is not a function"
interface KtorInterceptor {
    suspend operator fun invoke(sender: Sender, request: HttpRequestBuilder): HttpClientCall
}

val SecurityAttribute = AttributeKey<Boolean>("EdugmaSecurity")
