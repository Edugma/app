package io.edugma.features.account.payments.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.rememberAsyncImagePainter
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.features.account.domain.model.payments.PaymentMethod

@Composable
fun ColumnScope.PaymentBottomSheet(
    paymentMethod: PaymentMethod,
    openUri: () -> Unit,
) {
    BottomSheet(
        header = paymentMethod.title,
    ) {
        val painter = rememberAsyncImagePainter(model = paymentMethod.url)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable {
                    openUri()
                },
        )
        SpacerHeight(height = 20.dp)
        EdLabel(
            text = paymentMethod.description,
            iconPainter = painterResource(EdIcons.ic_fluent_info_24_regular),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(10.dp)
    }
}
