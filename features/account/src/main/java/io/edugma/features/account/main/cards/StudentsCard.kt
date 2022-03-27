package io.edugma.features.account.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentsCard(
    onClick: ClickListener
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(80.dp)
            .fillMaxWidth(0.6f),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = "Студенты")
        }
    }
}