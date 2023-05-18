package io.edugma.features.account.people.common.bottomSheets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.searchField.EdSearchField
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.ui.screen.BottomSheet

@OptIn(ExperimentalComposeUiApi::class)
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

        SpacerHeight(height = 10.dp)
        EdSearchField(
            modifier = Modifier.fillMaxWidth(),
            value = searchValue,
            onValueChange = onSearchValueChanged,
            placeholder = hint,
            keyboardActions = KeyboardActions(onDone = { searchClickListener() })
        )
        SpacerHeight(height = 20.dp)
        EdButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Применить",
            onClick = searchClickListener,
        )
    }
}
