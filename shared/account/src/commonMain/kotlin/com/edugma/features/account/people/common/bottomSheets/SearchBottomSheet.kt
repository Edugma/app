package com.edugma.features.account.people.common.bottomSheets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.molecules.button.EdButton
import com.edugma.core.designSystem.molecules.searchField.EdSearchField
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.ui.screen.BottomSheet

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColumnScope.SearchBottomSheet(
    title: String? = "Поиск",
    hint: String? = null,
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    onClick: () -> Unit,
) {
    BottomSheet(
        title = title,
        headerStyle = EdTheme.typography.headlineSmall,
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
            keyboardActions = KeyboardActions(onDone = { searchClickListener() }),
        )
        SpacerHeight(height = 20.dp)
        EdButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Найти",
            onClick = searchClickListener,
        )
    }
}
