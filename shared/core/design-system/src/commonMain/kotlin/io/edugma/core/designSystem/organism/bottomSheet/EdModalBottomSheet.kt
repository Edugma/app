package io.edugma.core.designSystem.organism.bottomSheet

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import androidx.compose.material.ModalBottomSheetState as Material2ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue as Material2ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState as rememberMaterial2ModalBottomSheetState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EdModalBottomSheet(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    containerColor: Color = EdTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = EdElevation.Level2.defaultElevation,
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetContent = sheetContent,
        modifier = modifier,
        sheetState = sheetState.m2State,
        sheetGesturesEnabled = true,
        sheetShape = EdTheme.shapes.extraLarge,
        sheetElevation = tonalElevation,
        sheetBackgroundColor = containerColor,
        sheetContentColor = contentColor,
        scrimColor = EdTheme.colorScheme.scrim.copy(alpha = 0.5f),
        content = content,
    )
}

enum class ModalBottomSheetValue {
    /**
     * The bottom sheet is not visible.
     */
    Hidden,

    /**
     * The bottom sheet is visible at full height.
     */
    Expanded,

    /**
     * The bottom sheet is partially visible at 50% of the screen height. This state is only
     * enabled if the height of the bottom sheet is more than 50% of the screen height.
     */
    HalfExpanded,
}

@OptIn(ExperimentalMaterialApi::class)
private fun ModalBottomSheetValue.toMaterial2(): Material2ModalBottomSheetValue {
    return when (this) {
        ModalBottomSheetValue.Hidden -> Material2ModalBottomSheetValue.Hidden
        ModalBottomSheetValue.Expanded -> Material2ModalBottomSheetValue.Expanded
        ModalBottomSheetValue.HalfExpanded -> Material2ModalBottomSheetValue.HalfExpanded
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun Material2ModalBottomSheetValue.toEdugma(): ModalBottomSheetValue {
    return when (this) {
        Material2ModalBottomSheetValue.Hidden -> ModalBottomSheetValue.Hidden
        Material2ModalBottomSheetValue.Expanded -> ModalBottomSheetValue.Expanded
        Material2ModalBottomSheetValue.HalfExpanded -> ModalBottomSheetValue.HalfExpanded
    }
}

@Stable
class ModalBottomSheetState @OptIn(ExperimentalMaterialApi::class)
internal constructor(
    internal val m2State: Material2ModalBottomSheetState,
) {
    @OptIn(ExperimentalMaterialApi::class)
    suspend fun show() {
        m2State.show()
    }

    @OptIn(ExperimentalMaterialApi::class)
    suspend fun hide() {
        m2State.hide()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmValueChange: (ModalBottomSheetValue) -> Boolean = { true },
    skipHalfExpanded: Boolean = true,
): ModalBottomSheetState {
    val m2State = rememberMaterial2ModalBottomSheetState(
        initialValue = initialValue.toMaterial2(),
        animationSpec = animationSpec,
        confirmValueChange = { confirmValueChange(it.toEdugma()) },
        skipHalfExpanded = skipHalfExpanded,
    )

    return ModalBottomSheetState(m2State)
}
