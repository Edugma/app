package io.edugma.data.nodes.api

import io.edugma.domain.nodes.model.Node
import io.edugma.domain.nodes.model.NodeContract
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Url

interface NodesService {
    companion object {
        private const val nodeListUrl =
            "https://raw.githubusercontent.com/Edugma/nodes/main/local/Russia/Moscow/list.json"
    }

    @GET
    fun getNodeContract(
        @Url url: String,
    ): Flow<Result<NodeContract>>

    @GET
    fun getNodeList(
        @Url url: String = nodeListUrl,
    ): Flow<Result<List<Node>>>
}
