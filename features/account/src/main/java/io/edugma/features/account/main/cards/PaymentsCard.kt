package io.edugma.features.account.main.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.domain.account.model.toLabel
import io.edugma.features.account.main.CurrentPayments
import io.edugma.features.base.core.utils.ClickListener

@Composable
fun PaymentsCard(
    modifier: Modifier = Modifier,
    currentPayments: CurrentPayments?,
    enabled: Boolean,
    onClick: ClickListener,
) {
    EdCard(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxWidth(1f)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        enabled = enabled,
        onClick = onClick,
    ) {
        Column(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Оплаты")
            }
            currentPayments?.let {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = it.type.toLabel(),
                        style = EdTheme.typography.bodySmall,
                        color = EdTheme.colorScheme.secondary,
                    )
                    Box(Modifier.fillMaxWidth()) {
                        Text(
                            text = "0",
                            style = EdTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.align(Alignment.TopStart),
                        )
                        Text(
                            text = it.sum.toString(),
                            style = EdTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.align(Alignment.TopEnd),
                        )
                    }
                    val progress = it.current.toFloat() / it.sum
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp, vertical = 3.dp),
                    ) {
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth(),
                            trackColor = EdTheme.colorScheme.onPrimary,
                            color = EdTheme.colorScheme.primary,
                        )
                    }
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val text = createRef()
                        Text(
                            text = it.current.toString(),
                            modifier = Modifier
                                .constrainAs(text) {
                                    linkTo(parent.start, parent.end, bias = progress)
                                },
                            textAlign = TextAlign.End,
                            style = EdTheme.typography.labelSmall,
                            color = EdTheme.colorScheme.tertiary,
                        )
                    }
                }
            }
        }
    }
}
