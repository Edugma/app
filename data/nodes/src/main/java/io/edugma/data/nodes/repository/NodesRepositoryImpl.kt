package io.edugma.data.nodes.repository

import io.edugma.data.base.consts.CacheConst
import io.edugma.data.base.consts.PrefConst
import io.edugma.data.base.local.CacheVersionLocalDS
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.get
import io.edugma.data.base.local.set
import io.edugma.data.base.model.map
import io.edugma.data.base.store.StoreImpl
import io.edugma.data.nodes.api.NodesService
import io.edugma.data.nodes.model.NodeContractDao
import io.edugma.domain.base.utils.Lce
import io.edugma.domain.nodes.model.Node
import io.edugma.domain.nodes.model.NodeContract
import io.edugma.domain.nodes.repository.NodesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.days

class NodesRepositoryImpl(
    private val service: NodesService,
    private val cachedDS: CacheVersionLocalDS,
    private val preferencesDS: PreferencesDS,
) : NodesRepository {
    private val nodeStore = StoreImpl<String, NodeContract>(
        fetcher = { key -> service.getNodeContract(key) },
        reader = { key ->
            cachedDS.getFlow<NodeContractDao>(CacheConst.SelectNode, expireAt)
                .map { it.map { it?.toModel() } }
        },
        writer = { key, data ->
            flowOf(cachedDS.save(NodeContractDao.from(key, data), CacheConst.Schedule))
        },
        expireAt = 1.days,
    )

    override fun selectNode(url: String) = flow {
        emit(preferencesDS.set(url, PrefConst.SelectedNode))
    }.flowOn(Dispatchers.IO)

    override fun selectNode(node: Node) = flow {
        emit(preferencesDS.set(node.contract, PrefConst.SelectedNode))
    }.flowOn(Dispatchers.IO)

    override fun getSelectNode() = flow {
        emit(preferencesDS.get<String>(PrefConst.SelectedNode))
    }.flowOn(Dispatchers.IO)

    override fun getNodeContract(url: String): Flow<Lce<NodeContract?>> =
        nodeStore.get(url)

    override fun getNodeList(): Flow<Result<List<Node>>> =
        service.getNodeList()
}
