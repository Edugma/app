package io.edugma.data.nodes.repository

import io.edugma.data.base.consts.CacheConst
import io.edugma.data.base.consts.PrefConst
import io.edugma.data.base.store.store
import io.edugma.data.nodes.api.NodesService
import io.edugma.domain.base.repository.CacheRepository
import io.edugma.domain.base.repository.SettingsRepository
import io.edugma.domain.base.repository.getFlow
import io.edugma.domain.base.repository.save
import io.edugma.domain.base.utils.Lce
import io.edugma.domain.nodes.model.Node
import io.edugma.domain.nodes.model.NodeContract
import io.edugma.domain.nodes.repository.NodesRepository
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration.Companion.days

class NodesRepositoryImpl(
    private val service: NodesService,
    private val settingsRepository: SettingsRepository,
    private val cacheRepository: CacheRepository,
) : NodesRepository {
    private val nodeStore = store {
        fetcher { key -> service.getNodeContract(key).getOrThrow() }
        cache {
            reader { key ->
                cacheRepository.getFlow<NodeContract>(CacheConst.SelectNode, expiresIn)
            }
            writer { key, data ->
                cacheRepository.save(CacheConst.SelectNode, data)
            }
            expiresIn(1.days)
        }
    }

    override suspend fun selectNode(url: String) {
        settingsRepository.saveString(PrefConst.SelectedNode, url)
    }

    override suspend fun selectNode(node: Node) {
        settingsRepository.saveString(PrefConst.SelectedNode, node.contract)
    }

    override suspend fun getSelectNode(): String? {
        return settingsRepository.getString(PrefConst.SelectedNode)
    }

    override fun getNodeContract(url: String): Flow<Lce<NodeContract?>> =
        nodeStore.get(url)

    override suspend fun getNodeList(): List<Node> {
        return service.getNodeList().getOrThrow()
    }
}
