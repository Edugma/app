package io.edugma.core.api.utils

import kotlinx.datetime.LocalTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

fun LocalTime.plus(other: LocalTime): LocalTime {
    // TODO can be overflow
    return LocalTime.fromNanosecondOfDay(
        this.toNanosecondOfDay() + other.toNanosecondOfDay(),
    )
}

fun LocalTime.plus(other: Duration): LocalTime {
    // TODO can be overflow
    return LocalTime.fromNanosecondOfDay(
        this.toNanosecondOfDay() + other.inWholeNanoseconds,
    )
}

fun LocalTime.minus(other: Duration): LocalTime {
    // TODO can be overflow
    return LocalTime.fromNanosecondOfDay(
        this.toNanosecondOfDay() - other.inWholeNanoseconds,
    )
}

val LocalTime.Companion.MIN
    get() = LocalTime(
        0,
        0,
        0,
        0,
    )

val LocalTime.Companion.MAX
    get() = LocalTime(
        23,
        59,
        59,
        999,
    )

fun LocalTime.copy(
    hour: Int = this.hour,
    minute: Int = this.minute,
    second: Int = this.second,
    nanosecond: Int = this.nanosecond,
): LocalTime {
    return LocalTime(
        hour = hour,
        minute = minute,
        second = second,
        nanosecond = nanosecond,
    )
}

fun LocalTime.untilSeconds(other: LocalTime): Long {
    val diff = other.toNanosecondOfDay() - this.toNanosecondOfDay()
    return diff.nanoseconds.inWholeSeconds
}

fun LocalTime.untilMinutes(other: LocalTime): Long {
    val diff = other.toNanosecondOfDay() - this.toNanosecondOfDay()
    return diff.nanoseconds.inWholeMinutes
}
