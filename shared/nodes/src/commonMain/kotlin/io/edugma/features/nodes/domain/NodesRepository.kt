package io.edugma.features.nodes.domain

import io.edugma.core.api.utils.LceFlow
import io.edugma.features.nodes.domain.model.Node
import io.edugma.features.nodes.domain.model.NodeContract

interface NodesRepository {
    suspend fun selectNode(url: String)
    suspend fun selectNode(node: Node)
    suspend fun getSelectNode(): String?
    fun getNodeContract(url: String): LceFlow<NodeContract>
    suspend fun getNodeList(): List<Node>
}
