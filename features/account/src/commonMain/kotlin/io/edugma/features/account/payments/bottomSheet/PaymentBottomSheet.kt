package io.edugma.features.account.payments.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.rememberAsyncImagePainter
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.core.utils.ClickListener
import org.jetbrains.compose.resources.ExperimentalResourceApi
import dev.icerock.moko.resources.compose.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PaymentBottomSheet(
    qrUrl: String,
    openUri: ClickListener,
) {
    BottomSheet(
        header = "QR код",
        verticalContentPadding = 15.dp,
    ) {
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
    }
}
