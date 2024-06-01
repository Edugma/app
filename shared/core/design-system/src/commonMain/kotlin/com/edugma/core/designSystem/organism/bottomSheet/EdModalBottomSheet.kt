package com.edugma.core.designSystem.organism.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomSheetDefaults as BottomSheetDefaults3
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet as ModalBottomSheet3
import androidx.compose.material3.ModalBottomSheetDefaults as ModalBottomSheetDefaults3
import androidx.compose.material3.SheetState as SheetState3
import androidx.compose.material3.SheetValue as SheetValue3
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.molecules.dragger.EdDragHandle
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.elevation.EdElevation
import com.edugma.core.designSystem.tokens.shapes.top
import com.edugma.core.designSystem.utils.BackHandler
import kotlinx.coroutines.launch
import androidx.compose.material3.rememberModalBottomSheetState as rememberModalBottomSheetState3

@Composable
fun EdModalBottomSheet(
    onDismissRequest: () -> Unit = {},
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    showDragHandle: Boolean = true,
    containerColor: Color = EdTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = EdElevation.Level2.defaultElevation,
    windowInsets: WindowInsets = WindowInsets(0),
    content: @Composable ColumnScope.() -> Unit,
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

    ModalBottomSheet3(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState.state3,
        sheetMaxWidth = BottomSheetDefaults3.SheetMaxWidth,
        shape = EdTheme.shapes.extraLarge.top(),
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = BottomSheetDefaults3.ScrimColor, // EdTheme.colorScheme.scrim.copy(alpha = 0.5f)
        dragHandle = if (showDragHandle) {
            {
                EdDragHandle(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )
            }
        } else {
            null
        },
        windowInsets = windowInsets,
        properties = ModalBottomSheetDefaults3.properties(),
        content = content,
    )
}

@Immutable
enum class SheetValue {
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
    PartiallyExpanded,
}

@OptIn(ExperimentalMaterial3Api::class)
private fun SheetValue3.toEdugma(): SheetValue {
    return when (this) {
        SheetValue3.Hidden -> SheetValue.Hidden
        SheetValue3.Expanded -> SheetValue.Expanded
        SheetValue3.PartiallyExpanded -> SheetValue.PartiallyExpanded
    }
}

@Stable
class SheetState internal constructor(
    internal val state3: SheetState3,
) {
    val isVisible: Boolean by state3::isVisible

    private var _showBottomSheet = mutableStateOf(false)
    val showBottomSheet: Boolean
        get() = _showBottomSheet.value

    val targetValue: SheetValue
        get() = state3.targetValue.toEdugma()

    val currentValue: SheetValue
        get() = state3.currentValue.toEdugma()

    suspend fun show() {
        _showBottomSheet.value = true
    }

    suspend fun hide() {
        if (_showBottomSheet.value == false) return

        state3.hide()
        if (!state3.isVisible) {
            _showBottomSheet.value = false
        }
    }

    fun setHide() {
        _showBottomSheet.value = false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SheetState

        return state3 == other.state3
    }

    override fun hashCode(): Int {
        return state3.hashCode()
    }
}

@Composable
fun rememberModalBottomSheetState(
    skipPartiallyExpanded: Boolean = false,
    confirmValueChange: (SheetValue) -> Boolean = { true },
): SheetState {
    // TODO Проверить, почему лямбды в rememberModalBottomSheetState не оборачивались
    // в remember
    val state3 = rememberModalBottomSheetState3(
        skipPartiallyExpanded = skipPartiallyExpanded,
        confirmValueChange = remember {
            { it: SheetValue3 -> confirmValueChange(it.toEdugma()) }
        },
    )

    return remember(state3) {
        SheetState(state3)
    }
}

@Composable
fun SheetState.bind(
    showBottomSheet: () -> Boolean,
    onClosed: () -> Unit
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(this) {
        snapshotFlow { showBottomSheet() }.collect {
            if (it) {
                scope.launch { this@bind.show() }
            } else {
                scope.launch { this@bind.hide() }
            }
        }
    }

    LaunchedEffect(this) {
        snapshotFlow { this@bind.showBottomSheet }.collect {
            if (!it) {
                onClosed()
            }
        }
    }
}
