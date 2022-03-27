package io.edugma.domain.nodes.repository

import io.edugma.domain.base.utils.Lce
import io.edugma.domain.nodes.model.NodeContract
import kotlinx.coroutines.flow.Flow

interface NodesRepository {
    fun selectNode(url: String): Flow<Result<Unit>>
    fun getSelectNode(): Flow<Result<String?>>
    fun getSNodeContract(url: String): Flow<Lce<NodeContract?>>
}