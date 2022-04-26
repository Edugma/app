package io.edugma.features.schedule.sources

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.elements.InitialAvatar
import io.edugma.features.base.elements.PrimaryTopAppBar
import org.koin.androidx.compose.getViewModel

@Composable
fun ScheduleSourcesScreen(viewModel: ScheduleSourcesViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()
    
    ScheduleSourcesContent(
        state,
        viewModel::exit,
        viewModel::onQueryChange,
        viewModel::onSelectSourceType,
        viewModel::onSelectSource
    )
}

@Composable
fun ScheduleSourcesContent(
    state: ScheduleSourceState,
    onBackClick: ClickListener,
    onQueryChange: Typed1Listener<String>,
    onSourceTypeSelected: Typed1Listener<ScheduleSources>,
    onSourceSelected: Typed1Listener<ScheduleSourceFull>
) {
    
    Column {
        PrimaryTopAppBar(
            title = "Выбор расписания",
            onBackClick = onBackClick
        )

        OutlinedTextField(
            value = state.query,
            onValueChange = onQueryChange,
            colors = TextFieldDefaults
                .outlinedTextFieldColors(textColor = MaterialTheme3.colorScheme.onBackground),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            placeholder = {
                WithContentAlpha(alpha = ContentAlpha.medium) {
                    Text(
                        text = "Поиск"
                    )
                }
            }
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(state.sourceTypes) { sourceType ->
                SourceType(
                    sourceType,
                    sourceType == state.selectedSourceType,
                    onSourceTypeSelected
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(state.filteredSources) { source ->
                SourceItem(source, onSourceSelected)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceType(
    sourceType: ScheduleSources,
    isSelected: Boolean,
    onSourceTypeSelected: Typed1Listener<ScheduleSources>
) {
    val color = if (isSelected) MaterialTheme3.colorScheme.primary else MaterialTheme3.colorScheme.surfaceVariant
    Card(
        onClick = { onSourceTypeSelected(sourceType) },
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        containerColor = color
    ) {
        Text(
            text = sourceType.toString(),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun SourceItem(source: ScheduleSourceFull, onItemClick: Typed1Listener<ScheduleSourceFull>) {
    Column(
        Modifier
            .clickable(onClick = { onItemClick(source) })
            .fillMaxWidth()
    ) {
        Spacer(Modifier.height(5.dp))
        Row {
            Spacer(Modifier.width(5.dp))
            val initials = when (source.type) {
                ScheduleSources.Group -> source.title.split('-').joinToString(separator = "") { it.take(1) }
                ScheduleSources.Teacher -> source.title.split(' ').joinToString(separator = "") { it.take(1) }
                ScheduleSources.Student -> source.title.split(' ').joinToString(separator = "") { it.take(1) }
                ScheduleSources.Place -> source.title
                ScheduleSources.Subject -> source.title.split(' ').joinToString(separator = "") { it.take(1) }
                ScheduleSources.Complex -> source.title.split(' ').joinToString(separator = "") { it.take(1) }
            }
            InitialAvatar(url = source.avatarUrl, initials)
            Spacer(Modifier.width(5.dp))
            Column {
                Text(
                    text = source.title,
                    style = MaterialTheme3.typography.titleSmall
                )
                WithContentAlpha(alpha = ContentAlpha.medium) {
                    Text(
                        text = source.description,
                        style = MaterialTheme3.typography.bodySmall
                    )
                }
            }
        }
        Spacer(Modifier.height(5.dp))
    }
}


//@Composable
//fun AutoSizeText(
//    text: String,
//    modifier: Modifier = Modifier,
//    color: Color = Color.Unspecified,
//    fontSize: TextUnit = TextUnit.Unspecified,
//    fontStyle: FontStyle? = null,
//    fontWeight: FontWeight? = null,
//    fontFamily: FontFamily? = null,
//    letterSpacing: TextUnit = TextUnit.Unspecified,
//    textDecoration: TextDecoration? = null,
//    textAlign: TextAlign? = null,
//    lineHeight: TextUnit = TextUnit.Unspecified,
//    overflow: TextOverflow = TextOverflow.Clip,
//    softWrap: Boolean = true,
//    maxLines: Int = Int.MAX_VALUE,
//    onTextLayout: (TextLayoutResult) -> Unit = {},
//    style: TextStyle = LocalTextStyle.current
//) {
//    var scaledTextStyle by remember { mutableStateOf(style) }
//    var readyToDraw by remember { mutableStateOf(false) }
//
//    Text(
//        text,
//        modifier = modifier.drawWithContent {
//            if (readyToDraw) {
//                drawContent()
//            }
//        },
//        color = color,
//        fontSize = fontSize,
//        fontStyle = fontStyle,
//        fontWeight = fontWeight,
//        fontFamily = fontFamily,
//        letterSpacing = letterSpacing,
//        textDecoration = textDecoration,
//        textAlign = textAlign,
//        lineHeight = lineHeight,
//        overflow = overflow,
//        //softWrap = softWrap,
//        softWrap = false,
//        maxLines = maxLines,
//        style = scaledTextStyle,
//        onTextLayout = { textLayoutResult ->
//            if (textLayoutResult.didOverflowWidth) {
//                scaledTextStyle =
//                    scaledTextStyle.copy(fontSize = scaledTextStyle.fontSize * (textLayoutResult.size.width / textLayoutResult.multiParagraph.width))
//            } else {
//                readyToDraw = true
//            }
//        }
//    )
//}