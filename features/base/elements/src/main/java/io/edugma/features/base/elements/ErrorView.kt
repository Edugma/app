package io.edugma.features.base.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.MaterialTheme3

@Composable
@Preview
fun ErrorView(message: String = "Что то пошло не так", retryAction: ClickListener = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = message,
            style = MaterialTheme3.typography.headlineSmall
        )
        SpacerWidth(width = 30.dp)
        PrimaryButton(onClick = retryAction) {
            Icon(Icons.Default.Refresh, "refresh")
            SpacerWidth(width = 5.dp)
            Text(
                text = "Повторить".uppercase(),
                style = MaterialTheme3.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}