package io.edugma.core.designSystem.molecules.avatar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
class EdAvatarSize(
    val size: Dp,
    val textSizes: List<TextUnit>,
) {
    companion object {
        val extraSmall
            get() = EdAvatarSize(
                size = 24.dp,
                textSizes = listOf(9.sp, 14.sp, 14.sp, 12.sp, 11.sp, 10.sp),
            )

        val small
            get() = EdAvatarSize(
                size = 32.dp,
                textSizes = listOf(10.sp, 15.sp, 15.sp, 13.sp, 12.sp, 11.sp),
            )

        val medium
            get() = EdAvatarSize(
                size = 48.dp,
                textSizes = listOf(13.sp, 17.sp, 17.sp, 15.sp, 14.sp, 13.sp),
            )

        val large
            get() = EdAvatarSize(
                size = 56.dp,
                textSizes = listOf(15.sp, 19.sp, 19.sp, 17.sp, 16.sp, 15.sp),
            )

        val extraLarge
            get() = EdAvatarSize(
                size = 64.dp,
                textSizes = listOf(17.sp, 21.sp, 21.sp, 19.sp, 18.sp, 17.sp),
            )
    }
}
