package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindFreePlaceCard(
    onFreePlaceClick: ClickListener
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onFreePlaceClick
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = stringResource(R.string.sch_find_free_place))
        }
    }
}