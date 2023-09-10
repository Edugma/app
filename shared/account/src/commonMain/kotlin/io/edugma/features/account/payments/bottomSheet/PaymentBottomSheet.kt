package io.edugma.features.account.payments.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.molecules.chip.EdChipForm
import io.edugma.core.designSystem.molecules.chip.EdSelectableChip
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.rememberAsyncImagePainter
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.core.utils.ClickListener

@Composable
fun PaymentBottomSheet(
    qrUrl: String,
    showCurrent: Boolean,
    openUri: ClickListener,
    showCurrentListener: ClickListener,
    showTotalListener: ClickListener,
) {
    BottomSheet(
        header = "QR код",
        verticalContentPadding = 5.dp,
    ) {
        Row(Modifier.horizontalScroll(rememberScrollState())) {
            EdSelectableChip(
                selectedState = showCurrent,
                chipForm = EdChipForm.roundedSquare,
                onClick = showCurrentListener,
            ) {
                Text(
                    text = "Текущая задолженность",
                    style = EdTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            EdSelectableChip(
                selectedState = !showCurrent,
                chipForm = EdChipForm.roundedSquare,
                onClick = showTotalListener,
            ) {
                Text(
                    text = "Общая задолженность",
                    style = EdTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        SpacerHeight(10.dp)
        val painter = rememberAsyncImagePainter(model = qrUrl)
        Image(
            painter = painter,
            contentDescription = "qr code",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable {
                    openUri()
                },
        )
        SpacerHeight(height = 20.dp)
        EdLabel(
            text = "Вы можете сделать скриншот экрана или скачать QR-код на устройство, затем открыть его в мобильном приложении вашего банка:\nОплата по QR-коду -> Загрузить изображение",
            iconPainter = painterResource(EdIcons.ic_fluent_info_24_regular),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(10.dp)
    }
}
