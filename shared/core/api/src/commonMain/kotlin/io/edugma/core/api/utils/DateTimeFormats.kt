package io.edugma.core.api.utils

enum class DateTimeFormat(
    private val dateFormat: DateFormat,
    private val timeFormat: TimeFormat,
    private val separator: String,
) {
    FULL(DateFormat.FULL, TimeFormat.HOURS_MINUTES, ", "),
    ;

    fun toFormat(): String {
        return dateFormat.toFormat() + separator + timeFormat.toFormat()
    }
}

data class DateFormat(
    private val format: String,
    private val formatWithoutYear: String,
    private val hideYearIfCurrent: Boolean = false,
) {
    fun toFormat(): String {
        return if (hideYearIfCurrent) {
            formatWithoutYear
        } else {
            format
        }
    }

    fun hideYear(): DateFormat = copy(hideYearIfCurrent = true)

    companion object {
        val FULL = DateFormat(
            format = "d MMMM yyyy",
            formatWithoutYear = "d MMMM",
        )
        val FULL_PRETTY = DateFormat(
            format = "d MMMM, yyyy",
            formatWithoutYear = "d MMMM",
        )
        val DAY_MONTH = DateFormat(
            format = "d MMMM",
            formatWithoutYear = "d MMMM",
        )
        val DAY_MONTH_SHORT = DateFormat(
            format = "d MMM",
            formatWithoutYear = "d MMM",
        )
        val WEEK_DAY_MONTH = DateFormat(
            format = "EEEE, d MMMM",
            formatWithoutYear = "EEEE, d MMMM",
        )
        val WEEK_DAY_MONTH_SHORT = DateFormat(
            format = "EEE, d MMM",
            formatWithoutYear = "EEE, d MMM",
        )
        val WEEK_DAY_MONTH_YEAR = DateFormat(
            format = "EEEE, d MMMM, yyyy",
            formatWithoutYear = "EEEE, d MMMM",
        )
        val WEEK = DateFormat(
            format = "EEEE",
            formatWithoutYear = "EEEE",
        )
        val WEEK_SHORT = DateFormat(
            format = "EEE",
            formatWithoutYear = "EEE",
        )
    }
}

enum class TimeFormat(private val format: String) {
    HOURS_MINUTES("HH:mm"),
    ;

    fun toFormat(): String {
        return format
    }
}
