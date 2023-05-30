package io.edugma.features.base.elements.dialogs.util

import androidx.compose.ui.geometry.Offset
import io.edugma.domain.base.utils.format
import io.edugma.domain.base.utils.minus
import io.edugma.domain.base.utils.plus
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.Duration.Companion.hours

internal fun Float.getOffset(angle: Double): Offset =
    Offset((this * cos(angle)).toFloat(), (this * sin(angle)).toFloat())

internal val LocalDate.yearMonth: Pair<Int, Month>
    get() = this.year to this.month

internal val LocalTime.isAM: Boolean
    get() = this.hour in 0..11

internal val LocalTime.simpleHour: Int
    get() {
        val tempHour = this.hour % 12
        return if (tempHour == 0) 12 else tempHour
    }

internal fun Month.getShortLocalName(): String =
    this.format("MMM")

internal fun Month.getFullLocalName() =
    this.format("MMMM")

internal fun DayOfWeek.getShortLocalName() =
    this.format("EE")

internal fun LocalTime.toAM(): LocalTime = if (this.isAM) this else this.minus(12.hours)
internal fun LocalTime.toPM(): LocalTime = if (!this.isAM) this else this.plus(12.hours)

internal fun LocalTime.noSeconds(): LocalTime = LocalTime(this.hour, this.minute)
