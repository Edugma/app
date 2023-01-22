package io.edugma.features.base.elements.dialogs.core

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 *  Adds a title with the given text to the dialog
 * @param text title text from a string literal
 * @param res title text from a string resource
 * @param center text is aligned to center when true
 */
@Composable
fun MaterialDialogScope.title(
    text: String? = null,
    @StringRes res: Int? = null,
    color: Color = MaterialTheme.colors.onSurface,
    style: TextStyle = MaterialTheme.typography.h6,
    center: Boolean = false
) {
    val titleText = getString(res, text)
    var modifier = Modifier
        .fillMaxWidth()
        .padding(start = 24.dp, end = 24.dp)
        .height(64.dp)
        .wrapContentHeight(Alignment.CenterVertically)

    modifier = modifier.then(
        Modifier.wrapContentWidth(
            if (center) {
                Alignment.CenterHorizontally
            } else {
                Alignment.Start
            }
        )
    )

    Text(
        text = titleText,
        color = color,
        style = style,
        modifier = modifier
    )
}

/**
 *  Adds a title with the given text and icon to the dialog
 * @param text title text from a string literal
 * @param textRes title text from a string resource
 * @param icon optional icon displayed at the start of the title
 */
@Composable
fun MaterialDialogScope.iconTitle(
    text: String? = null,
    @StringRes textRes: Int? = null,
    color: Color = MaterialTheme.colors.onSurface,
    style: TextStyle = MaterialTheme.typography.h6,
    icon: @Composable () -> Unit = {}
) {
    val titleText = getString(textRes, text)
    Row(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(Modifier.width(14.dp))
        Text(
            text = titleText,
            color = color,
            style = style
        )
    }
}

/**
 *  Adds paragraph of text to the dialog
 * @param text message text from a string literal
 * @param res message text from a string resource
 */
@Composable
fun MaterialDialogScope.message(
    text: String? = null,
    color: Color = MaterialTheme.colors.onSurface,
    style: TextStyle = MaterialTheme.typography.body1,
    @StringRes res: Int? = null
) {
    val messageText = getString(res, text)

    Text(
        text = messageText,
        color = color,
        style = style,
        modifier = Modifier
            .padding(bottom = 28.dp, start = 24.dp, end = 24.dp)
    )
}

/**
 * Create an view in the dialog with the given content and appropriate padding
 * @param content the content of the custom view
 */
@Composable
fun MaterialDialogScope.customView(content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(bottom = 28.dp, start = 24.dp, end = 24.dp)) {
        content()
    }
}
