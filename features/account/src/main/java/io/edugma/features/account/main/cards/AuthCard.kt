package io.edugma.features.account.main.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.ClickListener

@Composable
fun AuthCard(
    avatar: String?,
    name: String?,
    onClick: ClickListener,
) {
    EdCard(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .heightIn(180.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
    ) {
        Column(
            Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "Авторизация",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
            )
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    avatar?.let {
                        Image(
                            painter = rememberImagePainter(
                                data = avatar,
                                builder = {
                                    transformations(CircleCropTransformation())
                                },
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp),
                        )
                    }
                    name?.let {
                        Spacer(Modifier.height(5.dp))
                        Text(
                            text = name,
                            style = EdTheme.typography.labelMedium,
                            color = EdTheme.colorScheme.secondary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}
