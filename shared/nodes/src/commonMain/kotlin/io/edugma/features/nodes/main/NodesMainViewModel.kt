package io.edugma.features.nodes.main

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.ScheduleScreens
import io.edugma.features.nodes.domain.NodesRepository
import io.edugma.features.nodes.domain.model.Node

class NodesMainViewModel(
    private val nodesRepository: NodesRepository,
) : BaseViewModel<NodesMainState>(NodesMainState()) {
    init {
        launchCoroutine {
            val nodeList = nodesRepository.getNodeList()
            newState {
                copy(
                    nodes = nodeList,
                )
            }
        }
    }

    fun onNodeUrl(nodeUrl: String) {
        newState {
            copy(
                nodeUrl = nodeUrl,
            )
        }
    }

    fun onEnterNodeUrl() {
        launchCoroutine {
            nodesRepository.selectNode(stateFlow.value.nodeUrl)
            // router.navigateTo(HomeScreens.Main())
            tabMenuRouter.navigateTo(ScheduleScreens.Menu())
        }
    }

    fun onTabClick(tab: NodeTabs) {
        newState {
            copy(
                selectedTab = tab,
            )
        }
    }

    fun onNodeItemClick(node: Node) {
        launchCoroutine {
            nodesRepository.selectNode(stateFlow.value.nodeUrl)
            tabMenuRouter.navigateTo(ScheduleScreens.Menu())
            // router.navigateTo(HomeScreens.Main())
        }
    }
}

data class NodesMainState(
    val nodeUrl: String = "",
    val nodes: List<Node> = emptyList(),
    val selectedTab: NodeTabs = NodeTabs.ByList,
    val tabs: List<NodeTabs> = NodeTabs.entries,
)

enum class NodeTabs {
    ByList,
    ByUrl,
}
