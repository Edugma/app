package com.edugma.core.api.repository

import com.edugma.core.api.api.EdugmaApi
import com.edugma.core.api.api.Node
import com.edugma.core.api.utils.LceFlow

// TODO move from api
interface NodesRepository {
    suspend fun selectNode(url: String): Boolean
    suspend fun getSelectNode(): String?
    fun getNodeContract(url: String): LceFlow<EdugmaApi>
    suspend fun getSelectedContract(): LceFlow<EdugmaApi>?
    suspend fun getNodeList(): List<Node>
}
