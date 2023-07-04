package io.edugma.features.nodes.main

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
            mutateState {
                state = state.copy(
                    nodes = nodeList,
                )
            }
        }
    }

    fun onNodeUrl(nodeUrl: String) {
        mutateState {
            state = state.copy(
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
        mutateState {
            state = state.copy(
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
