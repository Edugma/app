package io.edugma.features.nodes.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.elements.PrimaryButton
import io.edugma.features.base.elements.SpacerHeight
import org.koin.androidx.compose.getViewModel

@Composable
fun NodesMainScreen(viewModel: NodesMainViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    NodesMainContent(
        state = state,
        onNodeUrl = viewModel::onNodeUrl,
        onEnterNodeUrl = viewModel::onEnterNodeUrl
    )
}

@Composable
private fun NodesMainContent(
    state: NodesMainState,
    onNodeUrl: Typed1Listener<String>,
    onEnterNodeUrl: ClickListener
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)) {
            OutlinedTextField(
                value = state.nodeUrl,
                onValueChange = onNodeUrl,
                modifier = Modifier.fillMaxWidth()
            )
            SpacerHeight(height = 32.dp)
            PrimaryButton(
                onClick = onEnterNodeUrl,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Применить")
            }
        }
    }
}