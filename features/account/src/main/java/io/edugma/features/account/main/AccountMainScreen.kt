package io.edugma.features.account.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.domain.account.model.print
import io.edugma.features.account.main.cards.AuthCard
import io.edugma.features.account.main.cards.PersonalCard
import io.edugma.features.account.main.cards.StudentsCard
import io.edugma.features.account.main.cards.UsualCard
import io.edugma.features.account.main.model.MenuUi
import io.edugma.features.base.core.utils.MaterialTheme3
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountMainScreen(viewModel: AccountMainViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    AccountContent(state) { it.action(viewModel) }
}

@ExperimentalMaterialApi
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
                val info = state.personal?.let { "${it.type.print()} ${it.course} курса группы ${it.group}" }
                PersonalCard(
                    info,
                    state.personal?.direction) { onClickListener.invoke(MenuUi.Personal) }
                StudentsCard {onClickListener.invoke(MenuUi.Students)}
            }
            AuthCard(
                state.personal?.avatarUrl,
                state.personal?.name
            ) {onClickListener.invoke(MenuUi.Auth)}
        }
        Row(
            Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            UsualCard(modifier = Modifier.fillMaxWidth(0.5f), name = "Преподаватели") { onClickListener.invoke(MenuUi.Teachers) }
            UsualCard(modifier = Modifier,name = "Одногруппники") { onClickListener.invoke(MenuUi.Classmates) }
        }
        Row(
            Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            UsualCard(modifier = Modifier.fillMaxWidth(0.5f),name = "Оплаты") { onClickListener.invoke(MenuUi.Payments) }
            UsualCard(modifier = Modifier,name = "Справки, заявления") { onClickListener.invoke(MenuUi.Applications) }
        }
        Row(
            Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            UsualCard(name = "Успеваемость") { onClickListener.invoke(MenuUi.Marks) }
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