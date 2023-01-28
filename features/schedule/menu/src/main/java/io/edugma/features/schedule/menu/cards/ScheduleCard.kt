package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import io.edugma.core.designSystem.organism.actionCard.EdActionCard
import io.edugma.core.designSystem.organism.actionCard.EdActionCardWidth
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.menu.R
import io.edugma.features.schedule.menu.ScheduleMenuState

@Composable
fun ScheduleCard(
    state: ScheduleMenuState.MainState,
    onScheduleClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    EdActionCard(
        title = stringResource(R.string.sch_schedule),
        subtitle = "Сегодня нет занятий",
        onClick = onScheduleClick,
        width = EdActionCardWidth.large,
        modifier = modifier,
    ) {
        Column(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.sch_relax_2),
            )
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = LottieConstants.IterateForever,
            )
            LottieAnimation(
                composition,
                progress,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}
