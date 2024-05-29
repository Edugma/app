package com.edugma.core.api.utils

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

val LocalTime.Companion.MIN: LocalTime by lazy {
    LocalTime(
        hour = 0,
        minute = 0,
        second = 0,
        nanosecond = 0,
    )
}

val LocalTime.Companion.MAX: LocalTime by lazy {
    LocalTime(
        hour = 23,
        minute = 59,
        second = 59,
        nanosecond = 999,
    )
}

val LocalTime.isMax: Boolean
    get() {
        return hour == 23 && minute == 59 && second == 59
    }

val LocalTime.isMin: Boolean
    get() {
        return hour == 0 && minute == 0 && second == 0 && nanosecond == 0
    }

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
