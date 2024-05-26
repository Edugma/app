package io.edugma.core.api.model

data class AppState(
    val nodeState: NodeState = NodeState.Initialization,
)

enum class NodeState {
    Initialization,
    Ready,
    Selection,
}
