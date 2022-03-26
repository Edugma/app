package io.edugma.data.nodes.repository

import io.edugma.data.base.consts.PrefConst
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.set
import io.edugma.data.nodes.api.NodesService
import io.edugma.domain.nodes.model.NodeContract
import io.edugma.domain.nodes.repository.NodesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NodesRepositoryImpl(
    private val api: NodesService,
    private val preferencesDS: PreferencesDS
) : NodesRepository {
    override fun selectNode(url: String) = flow {
        emit(preferencesDS.set(url, PrefConst.SelectedNode))
    }.flowOn(Dispatchers.IO)

    override fun getSelectedNodeContract(): Flow<Result<NodeContract>> {
        TODO("Not yet implemented")
    }
}