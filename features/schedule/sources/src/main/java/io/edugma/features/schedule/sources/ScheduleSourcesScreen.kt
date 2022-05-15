package io.edugma.features.schedule.sources

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.domain.schedule.model.source.ScheduleSourcesTabs
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.elements.*
import org.koin.androidx.compose.getViewModel

@Composable
fun ScheduleSourcesScreen(viewModel: ScheduleSourcesViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    ScheduleSourcesContent(
        state = state,
        onBackClick = viewModel::exit,
        onQueryChange = viewModel::onQueryChange,
        onTabSelected = viewModel::onSelectTab,
        onSourceSelected = viewModel::onSelectSource,
        onAddFavorite = viewModel::onAddFavorite,
        onDeleteFavorite = viewModel::onDeleteFavorite
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleSourcesContent(
    state: ScheduleSourceState,
    onBackClick: ClickListener,
    onQueryChange: Typed1Listener<String>,
    onTabSelected: Typed1Listener<ScheduleSourcesTabs>,
    onSourceSelected: Typed1Listener<ScheduleSourceFull>,
    onAddFavorite: Typed1Listener<ScheduleSourceFull>,
    onDeleteFavorite: Typed1Listener<ScheduleSourceFull>
) {

    Column {
        PrimaryTopAppBar(
            title = "Выбор расписания",
            onBackClick = onBackClick
        )

        PrimarySearchField(
            value = state.query,
            onValueChange = onQueryChange,
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
            item {
                SpacerWidth(10.dp)
            }
            items(state.tabs) { tab ->
                SourceTypeTab(
                    tab,
                    tab == state.selectedTab,
                    onTabSelected = onTabSelected
                )
            }
            item {
                SpacerWidth(10.dp)
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.filteredSources, key = { state.selectedTab to it.key }) { source ->
                SourceItem(
                    source = source,
                    isFavorite = state.selectedTab == ScheduleSourcesTabs.Favorite,
                    onItemClick = onSourceSelected,
                    onAddFavorite = onAddFavorite,
                    onDeleteFavorite = onDeleteFavorite,
                    modifier = Modifier.animateItemPlacement(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceTypeTab(
    tab: ScheduleSourcesTabs,
    isSelected: Boolean,
    onTabSelected: Typed1Listener<ScheduleSourcesTabs>
) {
    val color = if (isSelected)
        MaterialTheme3.colorScheme.secondaryContainer
    else
        MaterialTheme3.colorScheme.surface

    val title = when (tab) {
        ScheduleSourcesTabs.Favorite -> "Избранное"
        ScheduleSourcesTabs.Group -> "Группы"
        ScheduleSourcesTabs.Teacher -> "Преподаватели"
        ScheduleSourcesTabs.Student -> "Студенты"
        ScheduleSourcesTabs.Place -> "Места занятий"
        ScheduleSourcesTabs.Subject -> "Предметы"
        ScheduleSourcesTabs.Complex -> "Расширенный поиск"
    }

    TonalCard(
        onClick = { onTabSelected(tab) },
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
        color = color,
        shape = MaterialTheme3.shapes.small
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun SourceItem(
    source: ScheduleSourceFull,
    isFavorite: Boolean,
    onItemClick: Typed1Listener<ScheduleSourceFull>,
    onAddFavorite: Typed1Listener<ScheduleSourceFull>,
    onDeleteFavorite: Typed1Listener<ScheduleSourceFull>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .clickable(onClick = { onItemClick(source) })
            .fillMaxWidth()
    ) {
        Row(Modifier.padding(vertical = 5.dp)) {
            SpacerWidth(16.dp)
            val initials = when (source.type) {
                ScheduleSources.Group -> source.title.split('-')
                    .joinToString(separator = "") { it.take(1) }
                ScheduleSources.Teacher -> source.title.split(' ')
                    .joinToString(separator = "") { it.take(1) }
                ScheduleSources.Student -> source.title.split(' ')
                    .joinToString(separator = "") { it.take(1) }
                ScheduleSources.Place -> source.title
                ScheduleSources.Subject -> source.title.split(' ')
                    .joinToString(separator = "") { it.take(1) }
                ScheduleSources.Complex -> source.title.split(' ')
                    .joinToString(separator = "") { it.take(1) }
            }
            InitialAvatar(url = source.avatarUrl, initials)
            SpacerWidth(8.dp)
            Column(Modifier.weight(1f)) {
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
            IconButton(
                onClick = {
                    if (isFavorite) {
                        onDeleteFavorite(source)
                    } else {
                        onAddFavorite(source)
                    }
                }
            ) {
                val tintColor = if (isFavorite) {
                    MaterialTheme3.colorScheme.primary
                } else {
                    LocalContentColor.current
                }
                val painter = if (isFavorite) {
                    painterResource(FluentIcons.ic_fluent_star_24_filled)
                } else {
                    painterResource(FluentIcons.ic_fluent_star_24_regular)
                }
                Icon(
                    painter = painter,
                    contentDescription = null,
                    tint = tintColor
                )
            }
            SpacerWidth(16.dp)
        }
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