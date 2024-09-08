package com.edugma.core.network

import com.edugma.core.api.api.EdugmaHttpClient
import com.edugma.core.api.repository.UrlRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

class EdugmaHttpClientImpl(
    private val urlRepository: UrlRepository,
    private val httpClient: HttpClient,
) : EdugmaHttpClient {

    override suspend fun getInternal(name: String, builder: EdugmaHttpClient.Builder): HttpResponse {
        val name = "$name-get"

        val url = urlRepository.url(name, builder.paramsMap)
        return httpClient.get(url) {
            val queryParams = urlRepository.queryParams(name, builder.paramsMap)
            for ((name, value) in queryParams) {
                url {
                    parameters.append(name, value)
                }
            }

            // TODO if repeats (abc=abc, abc=abc, abc) or (abc=abc; abc=abc; abc)
            val headerParams = urlRepository.headerParams(name, builder.paramsMap)
            for ((name, value) in headerParams) {
                headers {
                    append(name, value)
                }
            }

            val withToken = urlRepository.isSecure(name)

            setAttributes {
                put(SecurityAttribute, withToken)
            }
        }
    }

    override suspend fun postInternal(
        name: String,
        builder: EdugmaHttpClient.PostBuilder,
    ): HttpResponse {
        val name = "$name-post"

        // TODO name-post-get
        // Учесть: что в post проверка идёт по name-post
        if (urlRepository.getMethod(name) == HttpMethod.Get) {
            return getInternal(name, builder)
        }

        val url = urlRepository.url(name, builder.paramsMap)
        return httpClient.post(url) {
            val queryParams = urlRepository.queryParams(name, builder.paramsMap)
            for ((name, value) in queryParams) {
                url {
                    parameters.append(name, value)
                }
            }

            // TODO if repeats (abc=abc, abc=abc, abc) or (abc=abc; abc=abc; abc)
            val headerParams = urlRepository.headerParams(name, builder.paramsMap)
            for ((name, value) in headerParams) {
                headers {
                    append(name, value)
                }
            }

            val withToken = urlRepository.isSecure(name)

            setAttributes {
                put(SecurityAttribute, withToken)
            }

            contentType(ContentType.Application.Json)
            setBody(builder.body, builder.typeInfo!!)
        }
    }
}
