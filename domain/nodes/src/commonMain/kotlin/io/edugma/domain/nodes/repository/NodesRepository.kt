package io.edugma.domain.nodes.repository

import io.edugma.domain.base.utils.Lce
import io.edugma.domain.nodes.model.Node
import io.edugma.domain.nodes.model.NodeContract
import kotlinx.coroutines.flow.Flow

interface NodesRepository {
    suspend fun selectNode(url: String)
    suspend fun selectNode(node: Node)
    suspend fun getSelectNode(): String?
    fun getNodeContract(url: String): Flow<Lce<NodeContract?>>
    suspend fun getNodeList(): List<Node>
}
