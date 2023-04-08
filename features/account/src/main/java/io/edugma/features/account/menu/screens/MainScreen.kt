package io.edugma.features.account.menu.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import io.edugma.core.designSystem.organism.accountSelector.EdAccountSelector
import io.edugma.core.designSystem.organism.iconCard.EdIconCard
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.designSystem.utils.cachedIconPainter
import io.edugma.domain.account.model.menu.CardType
import io.edugma.features.account.menu.MenuState
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed2Listener

@Composable
fun MainScreen(
    state: MenuState.Menu,
    onSignOut: ClickListener,
    onPersonalClick: ClickListener,
    cardClick: Typed2Listener<CardType, String?>
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
                EdAccountSelector(
                    state = AccountSelectorVO(state.personalData?.fullName.orEmpty(), state.personalData?.label.orEmpty(), state.personalData?.avatar),
                    onClick = onPersonalClick
                )
                SpacerHeight(height = 14.dp)
            }
        }

        SpacerHeight(height = 10.dp)

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
                        icon = cachedIconPainter(card.icon),
                    )
                }
            }
            SpacerHeight(height = 8.dp)
        }
    }
}
