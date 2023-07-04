package io.edugma.features.nodes.domain

import io.edugma.core.api.utils.Lce
import io.edugma.features.nodes.domain.model.Node
import io.edugma.features.nodes.domain.model.NodeContract
import kotlinx.coroutines.flow.Flow

interface NodesRepository {
    suspend fun selectNode(url: String)
    suspend fun selectNode(node: Node)
    suspend fun getSelectNode(): String?
    fun getNodeContract(url: String): Flow<Lce<NodeContract?>>
    suspend fun getNodeList(): List<Node>
}
