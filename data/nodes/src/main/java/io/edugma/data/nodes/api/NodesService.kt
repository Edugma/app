package io.edugma.data.nodes.api

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Url
import io.edugma.domain.nodes.model.Node
import io.edugma.domain.nodes.model.NodeContract

interface NodesService {
    companion object {
        private const val nodeListUrl =
            "https://raw.githubusercontent.com/Edugma/nodes/main/local/Russia/Moscow/list.json"
    }

    @GET
    suspend fun getNodeContract(
        @Url url: String,
    ): Result<NodeContract>

    @GET
    suspend fun getNodeList(
        @Url url: String = nodeListUrl,
    ): Result<List<Node>>
}
