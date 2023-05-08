package io.edugma.features.account.people.classmates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.domain.account.model.student.Student
import io.edugma.features.account.people.common.items.PeopleItem
import io.edugma.features.account.people.common.items.PeopleItemPlaceholder
import io.edugma.features.account.people.common.utlis.convertAndShare
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.isNull
import org.koin.androidx.compose.getViewModel

@Composable
fun ClassmatesScreen(viewModel: ClassmatesViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    FeatureScreen {
        ClassmatesContent(
            state,
            retryListener = viewModel::updateClassmates,
            backListener = viewModel::exit,
        )
    }
}

@Composable
fun ClassmatesContent(
    state: ClassmatesState,
    retryListener: ClickListener,
    backListener: ClickListener,
) {
    Column {
        EdTopAppBar(
            title = "Однокурсники",
            onNavigationClick = backListener,
            actions = {
                val students = state.data
                val context = LocalContext.current

                IconButton(
                    onClick = { students?.convertAndShare(context) },
                    enabled = !students.isNullOrEmpty()
                ) {
                    Icon(
                        painterResource(id = EdIcons.ic_fluent_share_24_regular),
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
