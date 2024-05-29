package com.edugma.features.nodes.main

import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.BaseViewModel
import com.edugma.core.navigation.ScheduleScreens
import com.edugma.core.api.repository.NodesRepository
import com.edugma.core.api.api.Node
import com.edugma.core.api.model.NodeState
import com.edugma.core.api.repository.AppStateRepository
import com.edugma.core.api.repository.UrlTemplateRepository
import com.edugma.core.navigation.MainDestination
import com.edugma.core.navigation.nodes.NodesScreens

class NodesMainViewModel(
    private val nodesRepository: NodesRepository,
    private val appStateRepository: AppStateRepository,
    private val urlTemplateRepository: UrlTemplateRepository,
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
            urlTemplateRepository.init()
            appStateRepository.newState(
                appStateRepository.state.value.copy(
                    nodeState = NodeState.Ready,
                ),
            )
            // router.navigateTo(HomeScreens.Main())
            tabMenuRouter.navigateTo(MainDestination.Schedule) {
                popUpTo(NodesScreens.Main) {
                    inclusive = true
                }
            }
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
            nodesRepository.selectNode(node.contract)
            urlTemplateRepository.init()
            appStateRepository.newState(
                appStateRepository.state.value.copy(
                    nodeState = NodeState.Ready,
                ),
            )
            tabMenuRouter.navigateTo(MainDestination.Schedule) {
                popUpTo(NodesScreens.Main) {
                    inclusive = true
                }
            }
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
