package io.edugma.core.network

import io.edugma.core.api.api.EdugmaHttpClient
import io.edugma.core.api.repository.UrlRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

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
        }
    }

    override suspend fun postInternal(
        name: String,
        builder: EdugmaHttpClient.PostBuilder,
    ): HttpResponse {
        val name = "$name-post"

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

            setBody(builder.body, builder.typeInfo!!)
        }
    }
}
