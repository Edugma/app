package com.edugma.features.nodes.data

import com.edugma.core.api.api.EdugmaApi
import com.edugma.core.api.api.Node
import com.edugma.core.api.api.convert
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class NodesService(
    private val client: HttpClient,
) {
    companion object {
        private const val nodeListUrl =
            "https://raw.githubusercontent.com/Edugma/nodes/main/local/Russia/Moscow/list.json"
    }

    suspend fun getNodeContract(url: String): EdugmaApi {
        val response = runCatching { client.get(url) }
        return client.convert<EdugmaApi>(response).getOrThrow()
    }

    suspend fun getNodeList(url: String = nodeListUrl): Result<List<Node>> {
        val response = runCatching { client.get(url) }
        return client.convert<List<Node>>(response)
    }
}
