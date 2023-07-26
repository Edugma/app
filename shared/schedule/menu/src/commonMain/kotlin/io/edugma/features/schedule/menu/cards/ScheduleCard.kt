package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.designSystem.atoms.lottie.EdLottie
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.atoms.lottie.rememberLottiePainter
import io.edugma.core.designSystem.organism.actionCard.EdActionCard
import io.edugma.core.designSystem.organism.actionCard.EdActionCardWidth
import io.edugma.core.resources.MR
import io.edugma.core.utils.ClickListener
import io.edugma.features.schedule.menu.ScheduleMenuState

@Composable
fun ScheduleCard(
    state: ScheduleMenuState.MainState,
    onScheduleClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdActionCard(
        title = stringResource(MR.strings.sch_schedule),
        subtitle = "Сегодня нет занятий",
        onClick = onScheduleClick,
        width = EdActionCardWidth.large,
        modifier = modifier,
    ) {
        Column(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            val painter = rememberLottiePainter(
                source = LottieSource.Asset(MR.assets.sch_relax_2),
                alternativeUrl = "https://raw.githubusercontent.com/Edugma/resources/main/42410-sleeping-polar-bear.gif",
            )
            EdLottie(
                lottiePainter = painter,
                modifier = Modifier
                    .width(100.dp),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}
