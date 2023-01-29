package io.edugma.features.account.classmates

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.account.students.Student
import io.edugma.features.account.teachers.TeacherPlaceholder
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.isNull
import io.edugma.features.base.elements.ErrorView
import org.koin.androidx.compose.getViewModel

@Composable
fun ClassmatesScreen(viewModel: ClassmatesViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()
    ClassmatesContent(
        state,
        retryListener = viewModel::updateClassmates,
        backListener = viewModel::exit,
    )
}

@Composable
fun ClassmatesContent(
    state: ClassmatesState,
    retryListener: ClickListener,
    backListener: ClickListener,
) {
    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing), onRefresh = retryListener) {
        Column {
            AppBar(state = state, backListener = backListener)
            LazyColumn(Modifier.fillMaxSize()) {
                when {
                    state.isError && state.data.isNull() -> {
                        item { ErrorView(retryAction = retryListener) }
                    }
                    state.placeholders -> {
                        items(3) {
                            TeacherPlaceholder()
                        }
                    }
                    else -> {
                        items(
                            count = state.data?.size ?: 0,
                            key = { state.data?.get(it)?.id ?: it },
                        ) {
                            Student(student = state.data!![it]) {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppBar(
    state: ClassmatesState,
    backListener: ClickListener,
) {
    ConstraintLayout(Modifier.fillMaxWidth()) {
        val (content, filter) = createRefs()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(content) {
                linkTo(parent.start, filter.start)
                width = Dimension.fillToConstraints
            },
        ) {
            IconButton(onClick = backListener) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Назад",
                )
            }
            SpacerWidth(width = 15.dp)
            Text(
                text = "Однокурсники",
                style = EdTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Row(
            modifier = Modifier.constrainAs(filter) {
                linkTo(parent.top, parent.bottom)
                end.linkTo(parent.end)
            },
        ) {
            val students = state.data
                ?.mapIndexed { index, student -> "${index + 1}. ${student.getFullName()}" }
            if (students?.isNotEmpty() == true) {
                val context = LocalContext.current
                IconButton(
                    onClick = {
                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, students.joinToString("\n"))
                            type = "text/plain"
                        }
                            .let { Intent.createChooser(it, null) }
                            .also(context::startActivity)
                    },
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Поделиться",
                    )
                }
            }
        }
    }
}
