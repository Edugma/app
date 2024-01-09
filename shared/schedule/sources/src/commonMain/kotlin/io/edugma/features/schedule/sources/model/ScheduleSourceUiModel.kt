package io.edugma.features.schedule.sources.model

import androidx.compose.runtime.Immutable
import io.edugma.core.api.model.ListItemUiModel
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull

@Immutable
data class ScheduleSourceUiModel(
    val isFavorite: Boolean,
    val id: String,
    val type: String,
    val title: String,
    val description: String?,
    val avatar: String?,
) : ListItemUiModel() {
    override val listContentType: Any = 1
}

fun ScheduleSourceFull.toUiModel(isFavorite: Boolean = false): ScheduleSourceUiModel {
    return ScheduleSourceUiModel(
        isFavorite = isFavorite,
        id = id,
        type = type,
        title = title,
        description = description,
        avatar = avatar,
    )
}

fun ScheduleSourceUiModel.toDtoModel(): ScheduleSourceFull {
    return ScheduleSourceFull(
        id = id,
        type = type,
        title = title,
        description = description,
        avatar = avatar,
    )
}
