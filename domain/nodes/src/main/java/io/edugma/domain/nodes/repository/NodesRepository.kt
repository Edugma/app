package io.edugma.domain.nodes.repository

import io.edugma.domain.nodes.model.NodeContract
import kotlinx.coroutines.flow.Flow

interface NodesRepository {
    fun selectNode(url: String): Flow<Result<Unit>>
    fun getSelectedNodeContract(): Flow<Result<NodeContract>>
}