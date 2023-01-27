package io.edugma.features.nodes.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.domain.nodes.model.Node
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.elements.SpacerHeight
import io.edugma.features.base.elements.SpacerWidth
import org.koin.androidx.compose.getViewModel

@Composable
fun NodesMainScreen(viewModel: NodesMainViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    NodesMainContent(
        state = state,
        onTabClick = viewModel::onTabClick,
        onNodeUrl = viewModel::onNodeUrl,
        onEnterNodeUrl = viewModel::onEnterNodeUrl,
        onNodeItemClick = viewModel::onNodeItemClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NodesMainContent(
    state: NodesMainState,
    onTabClick: Typed1Listener<NodeTabs>,
    onNodeUrl: Typed1Listener<String>,
    onEnterNodeUrl: ClickListener,
    onNodeItemClick: Typed1Listener<Node>,
) {
    Column(
        Modifier.fillMaxSize(),
    ) {
        NodeTabsContent(
            tabs = state.tabs,
            selectedTab = state.selectedTab,
            onTabSelected = onTabClick,
        )

        when (state.selectedTab) {
            NodeTabs.ByList -> {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(state.nodes) {
                        NodeContent(
                            node = it,
                            onClick = onNodeItemClick,
                        )
                    }
                }
            }
            NodeTabs.ByUrl -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Введите ссылку узла")
                    SpacerHeight(height = 16.dp)
                    OutlinedTextField(
                        value = state.nodeUrl,
                        onValueChange = onNodeUrl,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    SpacerHeight(height = 16.dp)
                    EdButton(
                        onClick = onEnterNodeUrl,
                        modifier = Modifier.fillMaxWidth(),
                        text = "Применить",
                    )
                    SpacerHeight(height = 32.dp)
                }
            }
        }
    }
}

@Composable
private fun NodeTabsContent(
    tabs: List<NodeTabs>,
    selectedTab: NodeTabs,
    onTabSelected: Typed1Listener<NodeTabs>,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
    ) {
        item {
            SpacerWidth(10.dp)
        }
        items(tabs) { tab ->
            NodeTab(
                tab,
                tab == selectedTab,
                onTabSelected = onTabSelected,
            )
        }
        item {
            SpacerWidth(10.dp)
        }
    }
}

@Composable
private fun NodeTab(
    tab: NodeTabs,
    isSelected: Boolean,
    onTabSelected: Typed1Listener<NodeTabs>,
) {
    val color = if (isSelected) {
        EdTheme.colorScheme.secondaryContainer
    } else {
        EdTheme.colorScheme.surface
    }

    val title = when (tab) {
        NodeTabs.ByList -> "Список"
        NodeTabs.ByUrl -> "URL"
    }

    EdCard(
        onClick = { onTabSelected(tab) },
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
        shape = EdTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = color,
        ),
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun NodeContent(
    node: Node,
    onClick: Typed1Listener<Node>,
) {
    EdCard(
        onClick = { onClick(node) },
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth(),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(node.image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp),
            )
            SpacerWidth(width = 16.dp)
            Text(
                text = node.name,
            )
        }
    }
}
