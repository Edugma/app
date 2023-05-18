package io.edugma.core.designSystem.molecules.chip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.card.EdCardDefaults
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.ifThen

@Composable
fun EdSelectableChip(
    modifier: Modifier = Modifier,
    chipForm: EdChipForm = EdChipForm.circle,
    selectedState: Boolean = false,
    defaultColor: Color = EdTheme.colorScheme.primary,
    selectedColor: Color = EdTheme.colorScheme.surface,
    onClick: (() -> Unit)? = null,
    body: @Composable () -> Unit = {}
) {
    EdCard(
        modifier = Modifier
            .padding(4.dp)
            .height(32.dp),
        colors = EdCardDefaults.cardColors(if (selectedState) defaultColor else selectedColor),
        shape = RoundedCornerShape(chipForm.shapeSize)
    ) {
        Row(
            modifier = modifier
                .widthIn(min = 50.dp)
                .height(32.dp)
                .ifThen(onClick != null) { clickable(onClick = onClick!!) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = {
                SpacerWidth(width = 15.dp)
                body()
                SpacerWidth(width = 15.dp)
            },
        )
    }
}
