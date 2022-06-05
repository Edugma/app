package io.edugma.features.base.elements

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.FluentIcons
import io.edugma.features.base.core.utils.MaterialTheme3


@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme3.shapes.small,
    colors: TextFieldColors = PrimaryTextFieldDefaults.textFieldColors()
) {
    TonalCard(
        shape = shape,
        modifier = modifier
    ) {
        TextField(
            value,
            onValueChange,
            textModifier,
            enabled,
            readOnly,
            textStyle,
            label,
            placeholder,
            leadingIcon,
            trailingIcon,
            isError,
            visualTransformation,
            keyboardOptions,
            keyboardActions,
            singleLine,
            maxLines,
            interactionSource,
            shape,
            colors
        )
    }
}

@Composable
fun PrimarySearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme3.shapes.small,
    colors: TextFieldColors = PrimaryTextFieldDefaults.textFieldColors()
) {
    PrimaryTextField(
        value,
        onValueChange,
        modifier,
        textModifier,
        enabled,
        readOnly,
        textStyle,
        label,
        placeholder,
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
        isError,
        visualTransformation,
        keyboardOptions,
        keyboardActions,
        singleLine,
        maxLines,
        interactionSource,
        shape,
        colors
    )
}

object PrimaryTextFieldDefaults {
    @Composable
    fun textFieldColors() = TextFieldDefaults.textFieldColors(
        textColor = LocalContentColor.current,
        containerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )
}

@Composable
fun SearchIcon24() {
    Icon(
        painter = painterResource(FluentIcons.ic_fluent_search_24_regular),
        contentDescription = null
    )
}

@Composable
fun DismissIcon20(onClick: ClickListener) {
   IconButton(onClick = onClick) {
       Icon(
           painter = painterResource(FluentIcons.ic_fluent_dismiss_20_filled),
           contentDescription = null
       )
   }
}