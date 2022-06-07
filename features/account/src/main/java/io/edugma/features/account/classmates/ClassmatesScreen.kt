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
import io.edugma.features.account.students.Student
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.elements.SpacerWidth
import org.koin.androidx.compose.getViewModel

@Composable
fun ClassmatesScreen(viewModel: ClassmatesViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    ClassmatesContent(
        state,
        retryListener = {viewModel.loadClassmates()},
        backListener = {viewModel.exit()}
    )
}

@Composable
fun ClassmatesContent(state: ClassmatesState,
                      retryListener: ClickListener,
                      backListener: ClickListener,
) {
    Column {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (content, filter) = createRefs()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(content) {
                    linkTo(parent.start, filter.start)
                    width = Dimension.fillToConstraints
                }) {
                IconButton(onClick = backListener) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
                SpacerWidth(width = 15.dp)
                Text(
                    text = "Однокурсники",
                    style = MaterialTheme3.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(modifier = Modifier.constrainAs(filter) {
                linkTo(parent.top, parent.bottom)
                end.linkTo(parent.end)
            }) {
                val students = state.data
                    .mapIndexed { index, student -> "${index + 1}. ${student.getFullName()}"}
                if (students.isNotEmpty()) {
                    val context = LocalContext.current
                    IconButton(onClick = {
                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, students.joinToString("\n"))
                            type = "text/plain"
                        }
                            .let { Intent.createChooser(it, null) }
                            .also(context::startActivity) }
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Поделиться"
                        )
                    }
                }
            }
        }
        LazyColumn {
            items(
                count = state.data.size,
                key = { state.data[it].id }
            ) {
                Student(student = state.data[it])
            }
        }

    }
}
