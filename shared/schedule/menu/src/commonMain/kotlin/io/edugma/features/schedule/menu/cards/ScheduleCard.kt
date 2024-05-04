package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import io.edugma.core.designSystem.atoms.lottie.EdLottie
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.organism.actionCard.EdActionCard
import io.edugma.core.designSystem.organism.actionCard.EdActionCardWidth
import io.edugma.core.utils.ClickListener
import io.edugma.features.schedule.menu.presentation.ScheduleMenuUiState

@Composable
fun ScheduleCard(
    state: ScheduleMenuUiState.MainUiState,
    onScheduleClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdActionCard(
        title = stringResource(Res.string.sch_schedule),
        subtitle = "Сегодня нет занятий",
        onClick = onScheduleClick,
        width = EdActionCardWidth.large,
        modifier = modifier,
    ) {
        Column(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            EdLottie(
                lottieSource = LottieSource.FileRes("files/sch_relax_2.json"),
                modifier = Modifier
                    .width(100.dp),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}
