package io.edugma.core.designSystem.organism.bottomSheet

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.molecules.dragger.EdDragHandle
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.designSystem.tokens.shapes.top
import io.edugma.core.designSystem.utils.BackHandler
import kotlinx.coroutines.launch
import androidx.compose.material.ModalBottomSheetState as Material2ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue as Material2ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState as rememberMaterial2ModalBottomSheetState

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EdModalBottomSheet(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    showDragHandle: Boolean = true,
    containerColor: Color = EdTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = EdElevation.Level2.defaultElevation,
    windowInsets: WindowInsets = WindowInsets(0),
    content: @Composable () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    BackHandler(
        enabled = sheetState.isVisible,
    ) {
        scope.launch {
            sheetState.hide()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { sheetState.isVisible }.collect {
            if (!it) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(windowInsets)
                    .fillMaxWidth(),
            ) {
                if (showDragHandle) {
                    EdDragHandle(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 3.dp)
                            .align(Alignment.CenterHorizontally),
                    )
                } else {
                    SpacerHeight(height = 15.dp)
                }
                sheetContent()
            }
        },
        modifier = modifier,
        sheetState = sheetState.m2State,
        sheetGesturesEnabled = true,
        sheetShape = EdTheme.shapes.extraLarge.top(),
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
    val isVisible: Boolean by m2State::isVisible

    @OptIn(ExperimentalMaterialApi::class)
    val targetState: ModalBottomSheetValue
        get() = m2State.targetValue.toEdugma()

    @OptIn(ExperimentalMaterialApi::class)
    val currentState: ModalBottomSheetValue
        get() = m2State.currentValue.toEdugma()

    @OptIn(ExperimentalMaterialApi::class)
    suspend fun show() {
        m2State.show()
    }

    @OptIn(ExperimentalMaterialApi::class)
    suspend fun hide() {
        m2State.hide()
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ModalBottomSheetState

        return m2State == other.m2State
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun hashCode(): Int {
        return m2State.hashCode()
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

    return remember(m2State) {
        ModalBottomSheetState(m2State)
    }
}
