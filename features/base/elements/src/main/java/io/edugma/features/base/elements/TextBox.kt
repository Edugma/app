package io.edugma.features.base.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.core.utils.WithContentAlpha

@Composable
fun TextBox(
    modifier: Modifier = Modifier,
    value: String,
    title: String? = null,
    passwordMode: Boolean = false,
    maxLines: Int = 1,
    onValueChange: Typed1Listener<String>
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults
            .outlinedTextFieldColors(textColor = MaterialTheme3.colorScheme.onBackground),
        modifier = modifier
            .fillMaxWidth(),
        visualTransformation = if (passwordMode) PasswordVisualTransformation() else VisualTransformation.None,
        placeholder = {
            title?.let {
                WithContentAlpha(alpha = ContentAlpha.medium) {
                    Text(
                        text = title
                    )
                }
            }
        },
        maxLines = maxLines
    )
}