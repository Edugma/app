package io.edugma.core.api.repository

import io.edugma.core.api.api.EdugmaApi
import io.edugma.core.api.api.Node
import io.edugma.core.api.utils.LceFlow

// TODO move from api
interface NodesRepository {
    suspend fun selectNode(url: String)
    suspend fun selectNode(node: Node)
    suspend fun getSelectNode(): String?
    fun getNodeContract(url: String): LceFlow<EdugmaApi>
    suspend fun getSelectedContract(): EdugmaApi?
    suspend fun getNodeList(): List<Node>
}
