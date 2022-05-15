package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.elements.TonalCard
import io.edugma.features.schedule.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeHistoryCard(
    onLessonsReviewClick: ClickListener
) {
    TonalCard(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxHeight()
            .fillMaxWidth(0.5f),
        onClick = onLessonsReviewClick,
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = "История изменений")
        }
    }
}