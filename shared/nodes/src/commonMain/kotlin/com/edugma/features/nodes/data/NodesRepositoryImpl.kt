package com.edugma.features.nodes.data

import com.edugma.core.api.api.EdugmaApi
import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.SettingsRepository
import com.edugma.core.api.repository.getFlow
import com.edugma.core.api.repository.save
import com.edugma.core.api.utils.LceFlow
import com.edugma.data.base.consts.CacheConst
import com.edugma.data.base.consts.PrefConst
import com.edugma.data.base.store.Store
import com.edugma.data.base.store.store
import com.edugma.core.api.repository.NodesRepository
import com.edugma.core.api.api.Node
import com.edugma.core.api.repository.get
import kotlin.time.Duration.Companion.days

class NodesRepositoryImpl(
    private val service: NodesService,
    private val settingsRepository: SettingsRepository,
    private val cacheRepository: CacheRepository,
) : NodesRepository {

    private val nodeStore: Store<String, EdugmaApi> = store {
        fetcher { key -> service.getNodeContract(key) }
        cache {
            reader { key ->
                cacheRepository.getFlow<EdugmaApi>(CacheConst.SelectedContract)
            }
            writer { key, data ->
                cacheRepository.save(CacheConst.SelectedContract, data)
            }
            expiresIn(1.days)
        }
    }

    override suspend fun selectNode(url: String) {
        settingsRepository.saveString(PrefConst.SelectedNode, url)
        val contract = service.getNodeContract(url)
        cacheRepository.save<EdugmaApi>(CacheConst.SelectedContract, contract)
    }

    override suspend fun selectNode(node: Node) {
        settingsRepository.saveString(PrefConst.SelectedNode, node.contract)
    }

    override suspend fun getSelectNode(): String? {
        return settingsRepository.getString(PrefConst.SelectedNode)
    }

    override fun getNodeContract(url: String): LceFlow<EdugmaApi> {
        return nodeStore.get(url)
    }

    override suspend fun getSelectedContract(): EdugmaApi? {
        // TODO Cache
        return cacheRepository.get<EdugmaApi>(CacheConst.SelectedContract)?.data
    }

    private val testNode = Node(
        name = "Тестовый сервер",
        otherNames = listOf(),
        image = "https://avatars.githubusercontent.com/u/97068545?s=200&v=4",
        contract = "https://raw.githubusercontent.com/Edugma/example-node/main/contract.json",
    )

    override suspend fun getNodeList(): List<Node> {
        return service.getNodeList().getOrThrow() + testNode
    }
}
