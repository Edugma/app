package io.edugma.features.account.personal

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.domain.account.model.Application
import io.edugma.domain.account.model.Order
import io.edugma.domain.account.model.Personal
import io.edugma.features.account.R
import io.edugma.features.account.personal.Columns.*
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import org.koin.androidx.compose.getViewModel
import kotlin.math.min

@Composable
fun PersonalScreen(viewModel: PersonalViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    FeatureScreen {
        PersonalContent(
            state = state,
            backListener = viewModel::exit,
            refreshListener = viewModel::update,
            typeListener = viewModel::setColumn,
        )
    }
}

@Composable
fun PersonalContent(
    state: PersonalState,
    backListener: ClickListener,
    refreshListener: ClickListener,
    typeListener: Typed1Listener<Columns>,
) {
    SwipeRefresh(state = rememberSwipeRefreshState(state.isRefreshing), onRefresh = refreshListener) {
        Column {
            val scrollState = rememberLazyListState()
            val scrollOffset: Float = min(
                1f,
                1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex),
            )
            CollapsingToolbar(
                state.personal,
                state.personalPlaceholders,
                scrollOffset,
                backListener,
            )
            LazyColumn(
                Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                state = scrollState,
            ) {
                when {
                    state.isError && state.personal.isNull() -> {
                        item { ErrorView(retryAction = refreshListener) }
                    }
                    state.personalPlaceholders -> {
                        item(key = "header") {
                            PersonalPlaceholder()
                        }
                        item(key = "selector") {
                            SelectableTypesRowPlaceholders()
                        }
                        items(3) {
                            SpacerHeight(height = 3.dp)
                            OrderPlaceholder()
                            Divider()
                        }
                    }
                    else -> {
                        item(key = "header") {
                            Personal(state.personal!!)
                        }
                        item(key = "selector") {
                            SelectableOneTypesRow(
                                types = values().toList(),
                                selectedType = state.selectedColumn,
                                nameMapper = { it.label },
                                clickListener = typeListener,
                            )
                        }
                        when (state.selectedColumn) {
                            Orders -> {
                                state.personal?.orders?.let { orders ->
                                    items(
                                        count = orders.size,
                                        key = { orders[it].name },
                                        itemContent = { index ->
                                            SpacerHeight(height = 3.dp)
                                            Order(orders[index])
                                            Divider()
                                        },
                                    )
                                }
                            }
                            Applications -> {
                                if (state.applicationsPlaceholders) {
                                    items(3) {
                                        ApplicationPlaceholder()
                                    }
                                } else {
                                    state.applications?.let { applications ->
                                        items(
                                            count = applications.size,
                                            key = { it },
                                            itemContent = { index ->
                                                SpacerHeight(height = 3.dp)
                                                Application(application = applications[index])
                                                Divider()
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CollapsingToolbar(
    personal: Personal?,
    placeholders: Boolean,
    scrollOffset: Float,
    onBack: ClickListener,
) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        val imageSize by animateDpAsState(targetValue = max(55.dp, 80.dp * scrollOffset))
        val (image, name, info, icon) = createRefs()
        IconButton(
            onClick = onBack,
            modifier = Modifier.constrainAs(icon) {
                start.linkTo(parent.start)
                linkTo(parent.top, info.bottom)
            },
        ) {
            Icon(painter = painterResource(FluentIcons.ic_fluent_arrow_left_20_filled), contentDescription = null)
        }
        personal?.let {
            Text(
                text = personal?.getNameSurname() ?: "О вас",
                style = EdTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .constrainAs(name) {
                        linkTo(start = icon.end, end = image.start, endMargin = 8.dp)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                    }
                    .placeholder(placeholders),
            )
            Text(
                text = "${personal?.degreeLevel} ${personal?.course} курса группы ${personal?.group}",
                style = EdTheme.typography.bodySmall,
                modifier = Modifier
                    .constrainAs(info) {
                        linkTo(start = icon.end, end = image.start, endMargin = 8.dp)
                        top.linkTo(name.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .placeholder(placeholders),
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(personal?.avatar)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .constrainAs(image) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .placeholder(placeholders),
        )
    }
}

@Composable
fun Personal(personal: Personal) {
    Column(Modifier.padding(8.dp)) {
        EdLabel(
            text = personal.faculty,
            iconPainter = painterResource(id = FluentIcons.ic_fluent_building_24_regular),
        )
        EdLabel(
            text = personal.specialty,
            iconPainter = painterResource(id = R.drawable.acc_ic_teacher_24),
        )
        personal.specialization?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(id = FluentIcons.ic_fluent_book_24_regular),
            )
        }
        EdLabel(
            text = "Номер зачетной книжки: ${personal.code}",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_album_24_regular),
        )
        EdLabel(
            text = "${personal.finance} ${personal.educationForm.lowercase()} основа обучения",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_money_24_regular),
        )
        EdLabel(
            text = "Год поступления ${personal.enterYear}",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular),
        )
        EdLabel(
            text = "Лет обучения ${personal.degreeLength}",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_timer_24_regular),
        )
    }
}

@Composable
fun PersonalPlaceholder() {
    Column(Modifier.padding(8.dp)) {
        EdLabel(
            text = "",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_building_24_regular),
            modifier = Modifier.placeholder(true),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = R.drawable.acc_ic_teacher_24),
            modifier = Modifier.placeholder(true),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_book_24_regular),
            modifier = Modifier.placeholder(true),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_album_24_regular),
            modifier = Modifier.placeholder(true),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_money_24_regular),
            modifier = Modifier.placeholder(true),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular),
            modifier = Modifier.placeholder(true),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_timer_24_regular),
            modifier = Modifier.placeholder(true),
        )
    }
}

@Composable
fun Order(order: Order) {
    Column(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 55.dp),
    ) {
        Box {
            Text(
                text = order.name,
                style = EdTheme.typography.titleMedium,
                modifier = Modifier
                    .defaultMinSize(minWidth = 100.dp),
            )
            Text(
                text = order.date?.format().orEmpty(),
                style = EdTheme.typography.labelMedium,
                modifier = Modifier
                    .defaultMinSize(minWidth = 50.dp)
                    .align(Alignment.CenterEnd),
            )
        }
        SpacerHeight(height = 15.dp)
        Text(
            text = order.description,
            style = EdTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun OrderPlaceholder() {
    Column(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 55.dp),
    ) {
        Box {
            Text(
                text = "",
                style = EdTheme.typography.titleMedium,
                modifier = Modifier
                    .defaultMinSize(minWidth = 100.dp)
                    .placeholder(true),
            )
            Text(
                text = "",
                style = EdTheme.typography.labelMedium,
                modifier = Modifier
                    .defaultMinSize(minWidth = 50.dp)
                    .align(Alignment.CenterEnd)
                    .placeholder(true),
            )
        }
        SpacerHeight(height = 15.dp)
        Text(
            text = "",
            style = EdTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .placeholder(true),
        )
    }
}

@Composable
fun Application(application: Application) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .padding(bottom = 5.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 55.dp),
    ) {
        Box(Modifier.fillMaxWidth()) {
            Text(
                text = application.question,
                style = EdTheme.typography.titleMedium,
            )
            Text(
                text = application.status.orEmpty(),
                style = EdTheme.typography.labelMedium,
                color = EdTheme.colorScheme.tertiary,
                textAlign = TextAlign.End,
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }

        Text(
            text = application.number,
            style = EdTheme.typography.labelSmall,
            color = EdTheme.colorScheme.secondary,
        )
        SpacerHeight(height = 10.dp)
        application.additionalInfo?.let {
            Text(text = it, style = EdTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ApplicationPlaceholder() {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .padding(bottom = 5.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 55.dp),
    ) {
        Box(Modifier.fillMaxWidth()) {
            Text(
                text = "",
                style = EdTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .defaultMinSize(minWidth = 100.dp)
                    .placeholder(true),
            )
            Text(
                text = "",
                style = EdTheme.typography.labelMedium,
                color = EdTheme.colorScheme.tertiary,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .defaultMinSize(minWidth = 50.dp)
                    .placeholder(true),
            )
        }

        Text(
            text = "",
            style = EdTheme.typography.labelSmall,
            color = EdTheme.colorScheme.secondary,
            modifier = Modifier
                .defaultMinSize(minWidth = 50.dp)
                .placeholder(true),
        )
        SpacerHeight(height = 10.dp)
        Text(
            text = "it",
            style = EdTheme.typography.bodySmall,
            modifier = Modifier
                .defaultMinSize(minWidth = 100.dp)
                .placeholder(true),
        )
    }
}
