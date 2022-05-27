package io.edugma.features.account.main.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.elements.TonalCard

@Composable
fun PaymentsCard(
    modifier: Modifier = Modifier,
    name: String,
    onClick: ClickListener
) {
    TonalCard(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxWidth(1f)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = name)
        }
    }
}