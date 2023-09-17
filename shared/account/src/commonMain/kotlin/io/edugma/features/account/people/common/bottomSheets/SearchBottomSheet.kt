package io.edugma.features.account.people.common.bottomSheets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.relocationRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.searchField.EdSearchField
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.ui.screen.BottomSheet
import kotlinx.coroutines.delay

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun SearchBottomSheet(
    title: String? = "Поиск",
    hint: String? = null,
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    onClick: () -> Unit,
) {
    BottomSheet(
        header = title,
        headerStyle = EdTheme.typography.headlineSmall,
        imePadding = true,
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        val searchClickListener = {
            keyboardController?.hide()
            focusManager.clearFocus()
            onClick()
        }

        val source = remember { MutableInteractionSource() }

        val focused = source.collectIsFocusedAsState()
        val relocationRequester = remember { BringIntoViewRequester() }
        val ime = keyboardAsState()
        LaunchedEffect(Unit) {
            snapshotFlow { ime.value && focused.value }.collect {
                if (it) {
                    delay(300)
                    relocationRequester.bringIntoView()
                }
            }
        }

        SpacerHeight(height = 10.dp)
        EdSearchField(
            modifier = Modifier.fillMaxWidth().bringIntoViewRequester(relocationRequester),
            value = searchValue,
            onValueChange = onSearchValueChanged,
            placeholder = hint,
            keyboardActions = KeyboardActions(onDone = { searchClickListener() }),
            interactionSource = source,
        )
        SpacerHeight(height = 20.dp)
        EdButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Найти",
            onClick = searchClickListener,
        )
    }
}
