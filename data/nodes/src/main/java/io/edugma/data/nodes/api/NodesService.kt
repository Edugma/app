package io.edugma.data.nodes.api

import io.edugma.domain.nodes.model.NodeContract
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Url

interface NodesService {
    @GET
    fun getNodeContract(
        @Url url: String
    ): Flow<Result<NodeContract>>
}