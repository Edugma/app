package com.edugma.core.designSystem.molecules.chip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.card.EdCard
import com.edugma.core.designSystem.atoms.spacer.SpacerWidth
import com.edugma.core.designSystem.utils.ifThen

@Composable
fun EdChip(
    modifier: Modifier = Modifier,
    chipForm: EdChipForm = EdChipForm.circle,
    iconPainter: Painter? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit = {},
) {
    EdCard(
        modifier = Modifier
            .padding(4.dp)
            .height(32.dp),
        shape = RoundedCornerShape(chipForm.shapeSize),
    ) {
        Row(
            modifier = modifier
                .padding(end = 15.dp, start = 5.dp)
                .widthIn(min = 50.dp, max = 100.dp)
                .height(32.dp)
                .ifThen(onClick != null) { clickable(onClick = onClick!!) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = {
                if (iconPainter != null) {
                    Icon(
                        painter = iconPainter,
                        contentDescription = null,
                        modifier = Modifier
                            .ifThen(onClick != null) { clickable(onClick = onClick!!) }
                            .size(20.dp),
                    )
                } else {
                    SpacerWidth(width = 10.dp)
                }
                content()
            },
        )
    }
}
