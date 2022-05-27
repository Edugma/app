package io.edugma.features.account.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.account.main.cards.*
import io.edugma.features.account.main.model.MenuUi
import io.edugma.features.base.core.utils.MaterialTheme3
import org.koin.androidx.compose.getViewModel

@Composable
fun AccountMainScreen(viewModel: AccountMainViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()
    AccountContent(state) { it.action(viewModel) }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountContent(state: AccountMenuState, onClickListener: (MenuUi) -> Unit) {
    Column(
        Modifier
            .padding(top = 20.dp, start = 4.dp, end = 4.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Сервисы",
            style = MaterialTheme3.typography.headlineMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(Modifier.height(20.dp))
        Row(
            Modifier
                .height(190.dp)
                .fillMaxWidth()
        ) {
            Column {
                val info = state.personal?.let { "${it.degreeLevel} ${it.course} курса группы ${it.group}" }
                PersonalCard(
                    info,
                    state.personal?.specialty) { onClickListener.invoke(MenuUi.Personal) }
                PerformanceCard(state.performance, state.showCurrentPerformance) {onClickListener.invoke(MenuUi.Marks)}
            }
            AuthCard(
                state.personal?.avatar,
                state.personal?.getFullName()
            ) {onClickListener.invoke(MenuUi.Auth)}
        }
        PaymentsCard(name = "Оплаты") { onClickListener.invoke(MenuUi.Payments) }
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
        ) {
            items(state.bottomMenu) {
                UsualCard(modifier = Modifier,name = it.label) { onClickListener.invoke(it) }
            }
        }
    }
}

private fun MenuUi.action(viewModel: AccountMainViewModel) {
    when (this) {
        MenuUi.Auth -> viewModel.navigateToAuth()
        MenuUi.Personal -> viewModel.navigateToPersonal()
        MenuUi.Students -> viewModel.navigateToStudents()
        MenuUi.Teachers -> viewModel.navigateToTeachers()
        MenuUi.Classmates -> viewModel.navigateToClassmates()
        MenuUi.Payments -> viewModel.navigateToPayments()
        MenuUi.Applications -> viewModel.navigateToApplications()
        MenuUi.Marks -> viewModel.navigateToMarks()
    }
}