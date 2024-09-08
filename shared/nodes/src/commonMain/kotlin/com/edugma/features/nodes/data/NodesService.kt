package com.edugma.features.nodes.data

import com.edugma.core.api.api.EdugmaApi
import com.edugma.core.api.api.Node
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Url

interface NodesService {
    companion object {
        private const val nodeListUrl =
            "https://raw.githubusercontent.com/Edugma/nodes/main/local/Russia/Moscow/list.json"
    }

    @GET
    suspend fun getNodeContract(
        @Url url: String,
    ): EdugmaApi

    @GET
    suspend fun getNodeList(
        @Url url: String = nodeListUrl,
    ): Result<List<Node>>
}
