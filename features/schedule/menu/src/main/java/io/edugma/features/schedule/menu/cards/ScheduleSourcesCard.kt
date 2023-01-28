package io.edugma.features.schedule.menu.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.molecules.avatar.EdAvatar
import io.edugma.core.designSystem.molecules.avatar.toAvatarInitials
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.MediumAlpha
import io.edugma.features.base.elements.SpacerWidth
import io.edugma.features.schedule.menu.ScheduleMenuState

@Composable
fun ScheduleSourcesCard(
    state: ScheduleMenuState.SourceState,
    onScheduleSourceClick: ClickListener,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(90.dp)
            .fillMaxWidth()
            .clickable { onScheduleSourceClick() },
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
        ) {
            state.selectedSource?.let {
                EdAvatar(
                    url = state.selectedSource.avatarUrl,
                    initials = remember {
                        state.selectedSource.title.toAvatarInitials()
                    },
                )
                SpacerWidth(width = 16.dp)
            }
            Column(Modifier.fillMaxWidth()) {
                Text(text = state.selectedSource?.title ?: "Выберите расписание")
                state.selectedSource?.description?.let {
                    MediumAlpha {
                        Text(
                            text = it,
                            style = EdTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}
