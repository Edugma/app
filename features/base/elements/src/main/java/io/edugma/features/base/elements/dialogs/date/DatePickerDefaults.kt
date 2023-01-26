package io.edugma.features.base.elements.dialogs.date

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Object to hold default values used by [datepicker]
 */
object DatePickerDefaults {
    /**
     * Initialises a [DatePickerColors] object which represents the different colors used by
     * the [datepicker] composable
     * @param headerBackgroundColor background color of header
     * @param headerTextColor color of text on the header
     * @param calendarHeaderTextColor color of text on the calendar header (year selector
     * and days of week)
     * @param dateActiveBackgroundColor background color of date when selected
     * @param dateActiveTextColor color of date text when selected
     * @param dateInactiveBackgroundColor background color of date when not selected
     * @param dateInactiveTextColor color of date text when not selected
     */
    @Composable
    fun colors(
        headerBackgroundColor: Color = MaterialTheme.colors.primary,
        headerTextColor: Color = MaterialTheme.colors.onPrimary,
        calendarHeaderTextColor: Color = MaterialTheme.colors.onBackground,
        dateActiveBackgroundColor: Color = MaterialTheme.colors.primary,
        dateInactiveBackgroundColor: Color = Color.Transparent,
        dateActiveTextColor: Color = MaterialTheme.colors.onPrimary,
        dateInactiveTextColor: Color = MaterialTheme.colors.onBackground,
    ): DatePickerColors {
        return DefaultDatePickerColors(
            headerBackgroundColor = headerBackgroundColor,
            headerTextColor = headerTextColor,
            calendarHeaderTextColor = calendarHeaderTextColor,
            dateActiveBackgroundColor = dateActiveBackgroundColor,
            dateInactiveBackgroundColor = dateInactiveBackgroundColor,
            dateActiveTextColor = dateActiveTextColor,
            dateInactiveTextColor = dateInactiveTextColor,
        )
    }
}
