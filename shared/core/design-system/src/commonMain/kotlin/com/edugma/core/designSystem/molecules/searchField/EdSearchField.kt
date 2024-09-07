package com.edugma.core.designSystem.molecules.searchField

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import edugma.shared.core.icons.generated.resources.ic_fluent_dismiss_20_filled
import edugma.shared.core.icons.generated.resources.ic_fluent_search_24_regular
import org.jetbrains.compose.resources.painterResource
import com.edugma.core.designSystem.molecules.textField.EdTextField
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.icons.EdIcons
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: String? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
    ),
) {
    EdTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        textStyle = textStyle,
        label = null,
        placeholder = placeholder,
        leadingIcon = {
            Box(Modifier.padding(start = 8.dp)) {
                SearchIcon24()
            }
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                Box(Modifier.padding(end = 8.dp)) {
                    DismissIcon20(onClick = { onValueChange("") })
                }
            }
        },
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1,
        interactionSource = interactionSource,
        shape = EdTheme.shapes.medium,
        colors = colors,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun SearchIcon24() {
    Icon(
        painter = painterResource(EdIcons.ic_fluent_search_24_regular),
        contentDescription = null,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun DismissIcon20(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(EdIcons.ic_fluent_dismiss_20_filled),
            contentDescription = null,
        )
    }
}
