package io.edugma.features.schedule.schedule_info.group_info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.elements.PrimaryTopAppBar
import io.edugma.features.schedule.schedule_info.place_info.PlaceInfoTabs
import org.koin.androidx.compose.getViewModel

@Composable
fun GroupInfoScreen(viewModel: GroupInfoViewModel = getViewModel(), id: String) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(id) {
        viewModel.setId(id)
    }

    GroupInfoContent(
        state = state,
        onBackClick = viewModel::exit,
        onTabSelected = viewModel::onTabSelected,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupInfoContent(
    state: GroupInfoState,
    onBackClick: ClickListener,
    onTabSelected: Typed1Listener<GroupInfoTabs>
) {
    Column(Modifier.fillMaxSize()) {
        PrimaryTopAppBar(
            title = state.groupInfo?.title ?: "",
            onBackClick = onBackClick
        )
        state.groupInfo?.let { groupInfo ->
            Text(text = groupInfo.title)
            Text(text = groupInfo.description)
            Text(text = groupInfo.course)
            Text(text = groupInfo.isEvening.toString())
        }
        LazyRow(Modifier.fillMaxWidth()) {
            items(state.tabs) {
                Card(
                    onClick = { onTabSelected(it) },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    val text = when (it) {
                        GroupInfoTabs.Schedule -> "Расписание"
                        GroupInfoTabs.Students -> "Студенты"
                    }

                    Text(
                        text = text,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}