package io.edugma.features.account.menu.screens.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.ClickListener

@Composable
fun UsualCard(
    modifier: Modifier = Modifier,
    name: String,
    enabled: Boolean,
    onClick: ClickListener,
) {
    EdCard(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(100.dp)
            .fillMaxWidth(),
        shape = EdTheme.shapes.large,
        enabled = enabled,
        onClick = onClick,
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = name)
        }
    }
}
