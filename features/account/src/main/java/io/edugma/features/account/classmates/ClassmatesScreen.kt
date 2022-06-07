package io.edugma.features.account.classmates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

//@Composable
//fun Student(student: Student?) {
//    Card(shape = MaterialTheme.shapes.medium, elevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
//        ConstraintLayout(modifier = Modifier.padding(5.dp)) {
//            val (name, image) = createRefs()
//            Text(text = student?.getFullName().orEmpty(),
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.constrainAs(name) {
//                    start.linkTo(image.end)
//                    end.linkTo(parent.end)
//                    width = Dimension.fillToConstraints
//                }
//                    .padding(start = 10.dp))
//            student?.avatar?.let {
//                Image(
//                    painter = rememberImagePainter(
//                        data = it,
//                        builder = {
//                            transformations(CircleCropTransformation())
//                        }
//                    ),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(100.dp)
//                        .constrainAs(image) {
//                            start.linkTo(parent.start)
//                            top.linkTo(parent.top)
//                        }
//                )
//            }
//
//        }
//    }
//}
