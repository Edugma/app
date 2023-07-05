package io.edugma.features.account.people.classmates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.isNull
import io.edugma.domain.account.model.student.Student
import io.edugma.features.account.people.common.items.PeopleItem
import io.edugma.features.account.people.common.items.PeopleItemPlaceholder
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun ClassmatesScreen(viewModel: ClassmatesViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    FeatureScreen {
        ClassmatesContent(
            state,
            retryListener = viewModel::updateClassmates,
            backListener = viewModel::exit,
            onShare = viewModel::onShare,
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ClassmatesContent(
    state: ClassmatesState,
    retryListener: ClickListener,
    backListener: ClickListener,
    onShare: ClickListener,
) {
    Column {
        EdTopAppBar(
            title = "Однокурсники",
            onNavigationClick = backListener,
            actions = {
                val students = state.data

                IconButton(
                    onClick = { onShare() },
                    enabled = !students.isNullOrEmpty(),
                ) {
                    Icon(
                        painterResource(EdIcons.ic_fluent_share_24_regular),
                        contentDescription = "Поделиться",
                    )
                }
            },
        )
        EdPullRefresh(refreshing = state.isRefreshing, onRefresh = retryListener) {
            if (state.isError && state.data.isNull()) {
                ErrorWithRetry(modifier = Modifier.fillMaxSize(), retryAction = retryListener)
            } else {
                ClassmatesList(state.data.orEmpty(), state.placeholders)
            }
        }
    }
}

@Composable
fun ClassmatesList(students: List<Student>, placeholders: Boolean) {
    LazyColumn(Modifier.fillMaxSize()) {
        if (placeholders) {
            items(3) {
                PeopleItemPlaceholder()
            }
        } else {
            items(students) {
                PeopleItem(title = it.getFullName(), description = it.getInfo(), avatar = it.avatar)
            }
        }
    }
}
