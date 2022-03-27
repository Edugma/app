package io.edugma.features.account.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                PersonalCard(
                    "Бакалавр 4 курса группы 181-721",
                    "Информационные системы и технологии") { onClickListener.invoke(MenuUi.Personal) }
                StudentsCard {onClickListener.invoke(MenuUi.Students)}
            }
            AuthCard(
                "https://sun1-87.userapi.com/s/v1/ig2/feSw8tuo4BrzUcXYwbxg51Be4whUnWNvTp7uAviBLqD4fewyzhZcvkJXFnOUJL0Rzf07-YiRCMkOKXH-F5C9e3-D.jpg?size=50x0&quality=96&crop=0,0,996,996&ava=1",
                "Дындин Александр Владимирович"
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