package io.edugma.features.nodes.main

import io.edugma.core.arch.mvi.updateState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.HomeScreens
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.nodes.domain.NodesRepository
import io.edugma.features.nodes.domain.model.Node

class NodesMainViewModel(
    private val nodesRepository: NodesRepository,
) : BaseViewModel<NodesMainState>(NodesMainState()) {
    init {
        launchCoroutine {
            val nodeList = nodesRepository.getNodeList()
            updateState {
                copy(
                    nodes = nodeList,
                )
            }
        }
    }

    fun onNodeUrl(nodeUrl: String) {
        updateState {
            copy(
                nodeUrl = nodeUrl,
            )
        }
    }

    fun onEnterNodeUrl() {
        launchCoroutine {
            nodesRepository.selectNode(state.value.nodeUrl)
            router.navigateTo(HomeScreens.Main())
        }
    }

    fun onTabClick(tab: NodeTabs) {
        updateState {
            copy(
                selectedTab = tab,
            )
        }
    }

    fun onNodeItemClick(node: Node) {
        launchCoroutine {
            nodesRepository.selectNode(state.value.nodeUrl)
            router.navigateTo(HomeScreens.Main())
        }
    }
}

data class NodesMainState(
    val nodeUrl: String = "",
    val nodes: List<Node> = emptyList(),
    val selectedTab: NodeTabs = NodeTabs.ByList,
    val tabs: List<NodeTabs> = NodeTabs.values().toList(),
)

enum class NodeTabs {
    ByList,
    ByUrl,
}
