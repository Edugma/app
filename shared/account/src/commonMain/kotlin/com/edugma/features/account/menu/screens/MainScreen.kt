package com.edugma.features.account.menu.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.organism.accountSelector.EdAccountSelector
import com.edugma.core.designSystem.organism.accountSelector.EdAccountSelectorPlaceholder
import com.edugma.core.designSystem.organism.iconCard.EdIconCard
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.shapes.bottom
import com.edugma.core.designSystem.utils.edPlaceholder
import com.edugma.core.designSystem.utils.rememberCachedIconPainter
import com.edugma.core.icons.EdIcons
import com.edugma.core.utils.ClickListener
import com.edugma.core.utils.Typed2Listener
import com.edugma.features.account.domain.model.menu.CardType
import com.edugma.features.account.menu.MenuState
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreen(
    state: MenuState.Menu,
    onSignOut: ClickListener,
    onPersonalClick: ClickListener,
    cardClick: Typed2Listener<CardType, String?>,
) {
    Column(
        Modifier.fillMaxSize(),
    ) {
        EdSurface(
            shape = EdTheme.shapes.large.bottom(),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 20.dp, start = 4.dp, end = 4.dp),
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Сервисы",
                        style = EdTheme.typography.headlineMedium,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f),
                    )
                    IconButton(onClick = onSignOut) {
                        Icon(
                            painter = painterResource(EdIcons.ic_fluent_sign_out_24_filled),
                            contentDescription = null,
                        )
                    }
                }
                SpacerHeight(height = 14.dp)
                if (state.account == null) {
                    EdAccountSelectorPlaceholder()
                } else {
                    EdAccountSelector(
                        state = state.account,
                        onClick = onPersonalClick,
                    )
                }
                SpacerHeight(height = 14.dp)
            }
        }

        SpacerHeight(height = 10.dp)

        if (state.cards.isEmpty() && state.isLoading) {
            CardPlaceholders()
        }

        state.cards.forEach { menuRow ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                menuRow.forEach { card ->
                    EdIconCard(
                        title = card.name,
                        subtitle = card.label,
                        modifier = Modifier.weight(card.weight),
                        onClick = { cardClick(card.type, card.url) },
                        icon = rememberCachedIconPainter(card.icon),
                    )
                }
            }
            SpacerHeight(height = 8.dp)
        }
    }
}

@Composable
fun ColumnScope.CardPlaceholders() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        EdIconCard(
            title = "",
            modifier = Modifier
                .weight(0.66f)
                .clip(CardDefaults.shape)
                .edPlaceholder(),
            onClick = {},
        )
        EdIconCard(
            title = "",
            modifier = Modifier
                .weight(0.33f)
                .clip(CardDefaults.shape)
                .edPlaceholder(),
            onClick = {},
        )
    }
    SpacerHeight(height = 8.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        EdIconCard(
            title = "",
            modifier = Modifier
                .weight(0.33f)
                .clip(CardDefaults.shape)
                .edPlaceholder(),
            onClick = {},
        )
        EdIconCard(
            title = "",
            modifier = Modifier
                .weight(0.33f)
                .clip(CardDefaults.shape)
                .edPlaceholder(),
            onClick = {},
        )
        EdIconCard(
            title = "",
            modifier = Modifier
                .weight(0.33f)
                .clip(CardDefaults.shape)
                .edPlaceholder(),
            onClick = {},
        )
    }
    SpacerHeight(height = 8.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        EdIconCard(
            title = "",
            modifier = Modifier
                .weight(1f)
                .clip(CardDefaults.shape)
                .edPlaceholder(),
            onClick = {},
        )
    }
    SpacerHeight(height = 8.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        EdIconCard(
            title = "",
            modifier = Modifier
                .weight(1f)
                .clip(CardDefaults.shape)
                .edPlaceholder(),
            onClick = {},
        )
    }
    SpacerHeight(height = 8.dp)
}
