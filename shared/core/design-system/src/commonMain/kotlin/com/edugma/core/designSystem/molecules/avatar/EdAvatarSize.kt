package com.edugma.core.designSystem.molecules.avatar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class EdAvatarSize(
    val size: Dp,
    val textSizes: List<TextUnit>,
) {
    companion object {
        val extraSmall: EdAvatarSize
            get() = EdAvatarSize(
                size = 24.dp,
                textSizes = listOf(9.sp, 14.sp, 14.sp, 12.sp, 11.sp, 10.sp),
            )

        val small: EdAvatarSize
            get() = EdAvatarSize(
                size = 32.dp,
                textSizes = listOf(10.sp, 15.sp, 15.sp, 13.sp, 12.sp, 11.sp),
            )

        val medium: EdAvatarSize
            get() = EdAvatarSize(
                size = 48.dp,
                textSizes = listOf(13.sp, 17.sp, 17.sp, 15.sp, 14.sp, 13.sp),
            )

        val large: EdAvatarSize
            get() = EdAvatarSize(
                size = 56.dp,
                textSizes = listOf(15.sp, 19.sp, 19.sp, 17.sp, 16.sp, 15.sp),
            )

        val extraLarge: EdAvatarSize
            get() = EdAvatarSize(
                size = 64.dp,
                textSizes = listOf(17.sp, 21.sp, 21.sp, 19.sp, 18.sp, 17.sp),
            )

        val xxl: EdAvatarSize
            get() = EdAvatarSize(
                size = 80.dp,
                textSizes = listOf(21.sp, 25.sp, 25.sp, 23.sp, 22.sp, 21.sp),
            )

        val xxxl: EdAvatarSize
            get() = EdAvatarSize(
                size = 100.dp,
                textSizes = listOf(26.sp, 30.sp, 30.sp, 28.sp, 27.sp, 26.sp),
            )
    }
}
