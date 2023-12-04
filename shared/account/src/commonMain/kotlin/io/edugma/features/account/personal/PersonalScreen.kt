package io.edugma.features.account.personal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.api.utils.format
import io.edugma.core.api.utils.getInitials
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.avatar.EdAvatar
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.designSystem.utils.SecondaryContent
import io.edugma.core.designSystem.utils.edPlaceholder
import io.edugma.core.designSystem.utils.statusBarsPadding
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.account.domain.model.Order
import io.edugma.features.account.domain.model.Personal
import io.edugma.features.account.domain.model.applications.Application

@Composable
fun PersonalScreen(viewModel: PersonalViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

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
        EdSurface(shape = EdTheme.shapes.large.bottom()) {
            Toolbar(
                state.personal,
                state.personalPlaceholders,
                backListener,
            )
        }
        EdPullRefresh(
            refreshing = state.isRefreshing,
            onRefresh = refreshListener,
        ) {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp)
                    .fillMaxSize(),
            ) {
                when {
                    state.isError && state.personal.isNull() -> {
                        ErrorWithRetry(retryAction = refreshListener)
                    }
                    state.personalPlaceholders -> {
                        PersonalPlaceholder()
                    }
                    else -> {
                        state.personal?.let {
                            Personal(state.personal)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Toolbar(
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
                text = personal?.name ?: "О вас",
                style = EdTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .edPlaceholder(placeholders),
            )
            EdLabel(
                text = personal?.description.orEmpty(),
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
            initials = personal?.let { getInitials(it.name) },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Personal(personal: Personal) {
    FlowRow(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        personal.data.forEach { (title, value) ->
            EdCard(
                modifier = Modifier,
                shape = EdTheme.shapes.small,
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 10.dp),
                ) {
                    SecondaryContent {
                        EdLabel(
                            text = title,
                            style = EdTheme.typography.labelSmall,
                        )
                    }
                    SpacerHeight(height = 1.dp)
                    EdLabel(
                        text = value,
                        style = EdTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun PersonalPlaceholder() {
    Column(Modifier.padding(8.dp)) {
        EdLabel(
            text = "",
            iconPainter = painterResource(EdIcons.ic_fluent_building_24_regular),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(EdIcons.ic_fluent_hat_graduation_24_filled),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(EdIcons.ic_fluent_book_24_regular),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(EdIcons.ic_fluent_album_24_regular),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(EdIcons.ic_fluent_money_24_regular),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_24_regular),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .edPlaceholder(),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(EdIcons.ic_fluent_timer_24_regular),
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
