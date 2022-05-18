package io.edugma.features.account.main.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.account.main.CurrentPerformance
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.elements.TonalCard

@Composable
fun StudentsCard(
    performance: CurrentPerformance?,
    onClick: ClickListener
) {
    TonalCard(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(80.dp)
            .fillMaxWidth(0.6f),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Column {
                Text(text = "Успеваемость")
                performance?.let {
                    Row {
                        Text(text = "${it.lastSemester.keys.first()} -  ${performance.lastSemester[it.lastSemester.keys.first()]}%")
                    }
                    Row {

                    }
                }

            }

        }
    }
}