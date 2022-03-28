package io.edugma.features.account.students

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import io.edugma.domain.account.model.Student
import io.edugma.domain.account.model.print
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.FluentIcons
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.elements.ErrorView
import io.edugma.features.base.elements.TextBox
import io.edugma.features.base.elements.TextWithIcon
import io.edugma.features.base.elements.placeholder
import org.koin.androidx.compose.getViewModel

@Composable
fun StudentsScreen(viewModel: StudentsViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    StudentsContent(state,
        retryListener = {viewModel.loadStudents()},
        backListener = {viewModel.exit()},
        inputListener = {viewModel.inputName(it)})
}

@Composable
fun StudentsContent(state: StudentsState,
                      retryListener: ClickListener,
                      backListener: ClickListener,
                      inputListener: Typed1Listener<String>
) {
    Column(verticalArrangement = Arrangement.Center) {
        Row(modifier = Modifier.padding(end = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = backListener) {
                Icon(painter = painterResource(FluentIcons.ic_fluent_arrow_left_20_filled), contentDescription = null)
            }
            TextBox(
                modifier = Modifier.fillMaxWidth(),
                value = state.query,
                title = "ФИО или группа студента",
                onValueChange = inputListener)
        }
        LazyColumn(modifier = Modifier.padding(8.dp)) {
                if (state.isPlaceholders) {
                    items(5) {
                        Student(null, placeholders = true)
                    }
                } else {
                    items(state.data) {
                        Student(it)
                    }
                }
        }
    }
}

@Composable
fun Student(student: Student?, placeholders: Boolean = false) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    ConstraintLayout(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()
        .clickable { isExpanded = !isExpanded }
    ) {
        val (name, image, type, divider) = createRefs()
        Text(text = student?.name.orEmpty(),
            style = MaterialTheme3.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .constrainAs(name) {
                    start.linkTo(image.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(start = 10.dp)
                .placeholder(placeholders)
        )
        Column(modifier = Modifier.padding(start = 10.dp).constrainAs(type) {
            linkTo(name.bottom, divider.top, bias = 0f)
            linkTo(name.start, parent.end)
            width = Dimension.fillToConstraints
        }) {
            if (placeholders) {
                TextWithIcon(
                    text = "",
                    icon = painterResource(id = R.drawable.acc_ic_teacher_24),
                    modifier = Modifier.placeholder(true)
                )
            } else {
                student?.educationType?.print()?.let {
                    TextWithIcon(
                        text = it,
                        icon = painterResource(id = R.drawable.acc_ic_teacher_24)
                    )
                }
                if (isExpanded) {
                    student?.specialization?.let {
                        TextWithIcon(text = it, icon = painterResource(id = FluentIcons.ic_fluent_book_24_regular))
                    }
                    student?.direction?.let {
                        TextWithIcon(text = it, icon = painterResource(id = FluentIcons.ic_fluent_book_open_24_regular))
                    }
                    student?.group?.let {
                        TextWithIcon(text = it, icon = painterResource(id = FluentIcons.ic_fluent_people_24_regular))
                    }
                }
            }
        }
        Image(
            painter = rememberImagePainter(
                data = student?.avatar,
                builder = {
                    transformations(CircleCropTransformation())
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    bottom.linkTo(divider.top)
                    linkTo(parent.top, divider.top, bias = 0f)
                }
                .placeholder(placeholders)
        )
        Divider(modifier = Modifier.constrainAs(divider) {
            start.linkTo(image.end)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
            width = Dimension.fillToConstraints
        })
    }
}
