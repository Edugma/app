package io.edugma.features.account.personal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.avatar.EdAvatar
import io.edugma.core.designSystem.organism.chipRow.EdSelectableChipRow
import io.edugma.core.designSystem.organism.chipRow.EdSelectableChipRowPlaceholders
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.designSystem.utils.edPlaceholder
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.isNull
import io.edugma.domain.account.model.Application
import io.edugma.domain.account.model.Order
import io.edugma.domain.account.model.Personal
import io.edugma.domain.base.utils.format
import io.edugma.features.account.R
import io.edugma.features.account.personal.Columns.Applications
import io.edugma.features.account.personal.Columns.Orders
import io.edugma.features.account.personal.Columns.values

@Composable
fun PersonalScreen(viewModel: PersonalViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    FeatureScreen(statusBarPadding = false) {
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
    Column {
        val scrollState = rememberLazyListState()
        EdSurface(shape = EdTheme.shapes.large.bottom()) {
            CollapsingToolbar(
                state.personal,
                state.personalPlaceholders,
                backListener,
            )
        }
        EdPullRefresh(
            refreshing = state.isRefreshing,
            onRefresh = refreshListener,
        ) {
            LazyColumn(
                Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize(),
                state = scrollState,
            ) {
                when {
                    state.isError && state.personal.isNull() -> {
                        item { ErrorWithRetry(retryAction = refreshListener) }
                    }
                    state.personalPlaceholders -> {
                        item(key = "header") {
                            PersonalPlaceholder()
                        }
                        item(key = "selector") {
                            EdSelectableChipRowPlaceholders()
                        }
                        items(3) {
                            SpacerHeight(height = 3.dp)
                            OrderPlaceholder()
                            Divider()
                        }
                    }
                    else -> {
                        state.personal?.let {
                            item(key = "header") {
                                Personal(state.personal)
                            }
                        }
                        item(key = "selector") {
                            EdSelectableChipRow(
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
    onBack: ClickListener,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 16.dp, end = 16.dp, bottom = 10.dp),
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier,
        ) {
            Icon(
                painter = painterResource(EdIcons.ic_fluent_chevron_left_20_filled),
                contentDescription = null,
            )
        }
        Column(Modifier.weight(1f)) {
            EdLabel(
                text = personal?.getNameSurname() ?: "О вас",
                style = EdTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .edPlaceholder(placeholders),
            )
            EdLabel(
                text = "${personal?.degreeLevel} ${personal?.course} курса группы ${personal?.group}",
                style = EdTheme.typography.bodySmall,
                modifier = Modifier
                    .edPlaceholder(placeholders),
            )
        }
        EdAvatar(
            url = personal?.avatar,
            modifier = Modifier
                .size(55.dp)
                .edPlaceholder(placeholders),
            initials = personal?.initials,
        )
    }
}

@Composable
fun Personal(personal: Personal) {
    Column(Modifier.padding(8.dp)) {
        EdLabel(
            text = personal.faculty,
            iconPainter = painterResource(id = EdIcons.ic_fluent_building_24_regular),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(height = 8.dp)
        EdLabel(
            text = personal.specialty,
            iconPainter = painterResource(id = R.drawable.acc_ic_teacher_24),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(height = 8.dp)
        personal.specialization?.let {
            EdLabel(
                text = it,
                iconPainter = painterResource(id = EdIcons.ic_fluent_book_24_regular),
                style = EdTheme.typography.bodyMedium,
            )
            SpacerHeight(height = 8.dp)
        }
        EdLabel(
            text = "Номер зачетной книжки: ${personal.code}",
            iconPainter = painterResource(id = EdIcons.ic_fluent_album_24_regular),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(height = 8.dp)
        EdLabel(
            text = "${personal.finance} ${personal.educationForm.lowercase()} основа обучения",
            iconPainter = painterResource(id = EdIcons.ic_fluent_money_24_regular),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(height = 8.dp)
        EdLabel(
            text = "Год поступления ${personal.enterYear}",
            iconPainter = painterResource(id = EdIcons.ic_fluent_calendar_ltr_24_regular),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(height = 8.dp)
        EdLabel(
            text = "Лет обучения ${personal.degreeLength}",
            iconPainter = painterResource(id = EdIcons.ic_fluent_timer_24_regular),
            style = EdTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun PersonalPlaceholder() {
    Column(Modifier.padding(8.dp)) {
        EdLabel(
            text = "",
            iconPainter = painterResource(id = EdIcons.ic_fluent_building_24_regular),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = R.drawable.acc_ic_teacher_24),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = EdIcons.ic_fluent_book_24_regular),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = EdIcons.ic_fluent_album_24_regular),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = EdIcons.ic_fluent_money_24_regular),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = EdIcons.ic_fluent_calendar_ltr_24_regular),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = EdIcons.ic_fluent_timer_24_regular),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
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
            EdLabel(
                text = order.name,
                style = EdTheme.typography.titleMedium,
                modifier = Modifier
                    .defaultMinSize(minWidth = 100.dp),
            )
            EdLabel(
                text = order.date?.format().orEmpty(),
                style = EdTheme.typography.labelMedium,
                modifier = Modifier
                    .defaultMinSize(minWidth = 50.dp)
                    .align(Alignment.CenterEnd),
            )
        }
        SpacerHeight(height = 15.dp)
        EdLabel(
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
            EdLabel(
                text = "",
                style = EdTheme.typography.titleMedium,
                modifier = Modifier
                    .defaultMinSize(minWidth = 100.dp)
                    .edPlaceholder(),
            )
            EdLabel(
                text = "",
                style = EdTheme.typography.labelMedium,
                modifier = Modifier
                    .defaultMinSize(minWidth = 50.dp)
                    .align(Alignment.CenterEnd)
                    .edPlaceholder(),
            )
        }
        SpacerHeight(height = 15.dp)
        EdLabel(
            text = "",
            style = EdTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .edPlaceholder(),
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
            EdLabel(
                text = application.question,
                style = EdTheme.typography.titleMedium,
            )
            EdLabel(
                text = application.status.orEmpty(),
                style = EdTheme.typography.labelMedium,
                color = EdTheme.colorScheme.tertiary,
                textAlign = TextAlign.End,
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }

        EdLabel(
            text = application.number,
            style = EdTheme.typography.labelSmall,
            color = EdTheme.colorScheme.secondary,
        )
        SpacerHeight(height = 10.dp)
        application.additionalInfo?.let {
            EdLabel(text = it, style = EdTheme.typography.bodySmall)
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
            EdLabel(
                text = "",
                style = EdTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .defaultMinSize(minWidth = 100.dp)
                    .edPlaceholder(),
            )
            EdLabel(
                text = "",
                style = EdTheme.typography.labelMedium,
                color = EdTheme.colorScheme.tertiary,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .defaultMinSize(minWidth = 50.dp)
                    .edPlaceholder(),
            )
        }

        EdLabel(
            text = "",
            style = EdTheme.typography.labelSmall,
            color = EdTheme.colorScheme.secondary,
            modifier = Modifier
                .defaultMinSize(minWidth = 50.dp)
                .edPlaceholder(),
        )
        SpacerHeight(height = 10.dp)
        EdLabel(
            text = "it",
            style = EdTheme.typography.bodySmall,
            modifier = Modifier
                .defaultMinSize(minWidth = 100.dp)
                .edPlaceholder(),
        )
    }
}
