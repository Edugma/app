package io.edugma.features.account.payments.bottomSheet

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.ui.screen.BottomSheet

@Composable
fun PaymentBottomSheet(
    qrUrl: String
) {
    BottomSheet(
        header = "QR код",
        verticalContentPadding = 15.dp
    ) {
        val context = LocalContext.current
        AsyncImage(
            model = qrUrl,
            contentDescription = "qr code",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable {
                    Intent(Intent.ACTION_VIEW, Uri.parse(qrUrl)).apply(context::startActivity)
                },
        )
        SpacerHeight(height = 20.dp)
        EdLabel(
            text = "Вы можете сделать скриншот экрана или скачать QR-код на устройство, затем открыть его в мобильном приложении вашего банка:\nОплата по QR-коду -> Загрузить изображение",
            iconPainter = painterResource(id = EdIcons.ic_fluent_info_24_regular),
            style = EdTheme.typography.bodyMedium
        )
    }
}
