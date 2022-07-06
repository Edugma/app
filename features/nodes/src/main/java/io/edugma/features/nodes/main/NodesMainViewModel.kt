package io.edugma.features.nodes.main

import androidx.lifecycle.viewModelScope
import io.edugma.domain.base.utils.onSuccess
import io.edugma.domain.nodes.model.Node
import io.edugma.domain.nodes.repository.NodesRepository
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.navigation.HomeScreens
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NodesMainViewModel(
    private val nodesRepository: NodesRepository
) : BaseViewModel<NodesMainState>(NodesMainState()) {
    init {
        viewModelScope.launch {
            nodesRepository.getNodeList().onSuccess {
                mutateState {
                    state = state.copy(
                        nodes = it
                    )
                }
            }.collect()
        }
    }

    fun onNodeUrl(nodeUrl: String) {
        mutateState {
            state = state.copy(
                nodeUrl = nodeUrl
            )
        }
    }

    fun onEnterNodeUrl() {
        viewModelScope.launch {
            nodesRepository.selectNode(state.value.nodeUrl)
                .onSuccess {
                    router.navigateTo(HomeScreens.Main)
                }.collect()
        }
    }

    fun onTabClick(tab: NodeTabs) {
        mutateState {
            state = state.copy(
                selectedTab = tab
            )
        }
    }

    fun onNodeItemClick(node: Node) {
        viewModelScope.launch {
            nodesRepository.selectNode(state.value.nodeUrl)
                .onSuccess {
                    router.navigateTo(HomeScreens.Main)
                }.collect()
        }
    }
}

data class NodesMainState(
    val nodeUrl: String = "",
    val nodes: List<Node> = emptyList(),
    val selectedTab: NodeTabs = NodeTabs.ByList,
    val tabs: List<NodeTabs> = NodeTabs.values().toList()
)

enum class NodeTabs {
    ByList,
    ByUrl
}