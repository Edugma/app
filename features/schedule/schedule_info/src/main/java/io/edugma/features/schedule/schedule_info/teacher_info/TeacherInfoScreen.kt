package io.edugma.features.schedule.schedule_info.teacher_info

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
import io.edugma.domain.schedule.model.teacher.description
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.elements.PrimaryTopAppBar
import org.koin.androidx.compose.getViewModel

@Composable
fun TeacherInfoScreen(viewModel: TeacherInfoViewModel = getViewModel(), id: String) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(id) {
        viewModel.setId(id)
    }

    TeacherInfoContent(
        state = state,
        onBackClick = viewModel::exit,
        onTabSelected = viewModel::onTabSelected,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeacherInfoContent(
    state: TeacherInfoState,
    onBackClick: ClickListener,
    onTabSelected: Typed1Listener<TeacherInfoTabs>
) {
    Column(Modifier.fillMaxSize()) {
        PrimaryTopAppBar(
            title = state.teacherInfo?.name ?: "",
            onBackClick = onBackClick
        )
        state.teacherInfo?.let { teacherInfo ->
            Text(text = teacherInfo.name)
            Text(text = teacherInfo.description)
        }
        LazyRow(Modifier.fillMaxWidth()) {
            items(state.tabs) {
                Card(
                    onClick = { onTabSelected(it) },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    val text = when (it) {
                        TeacherInfoTabs.Schedule -> "Расписание"
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