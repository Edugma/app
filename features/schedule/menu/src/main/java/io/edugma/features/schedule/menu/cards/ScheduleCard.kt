package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.schedule.menu.R
import io.edugma.features.schedule.menu.ScheduleMenuState

@Composable
fun ScheduleCard(
    state: ScheduleMenuState.MainState,
    onScheduleClick: ClickListener,
) {
    EdCard(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(150.dp)
            .fillMaxWidth(0.6f),
        onClick = onScheduleClick,
    ) {
        Column(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(text = stringResource(R.string.sch_schedule))
            Spacer(Modifier.height(4.dp))
//            Text(
//                text = stringResource(R.string.sch_now).uppercase(),
//                style = EdTheme.typography.labelSmall,
//                color = EdTheme.colorScheme.secondary
//            )
            Text(
                text = "Сегодня нет занятий".uppercase(),
                style = EdTheme.typography.labelSmall,
                color = EdTheme.colorScheme.secondary,
            )
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
//            WithContentAlpha(alpha = ContentAlpha.medium) {
//                Text(
//                    text = "Информационные системы и технологии",
//                    style = EdTheme.typography.bodySmall
//                )
//            }
        }
    }
}
