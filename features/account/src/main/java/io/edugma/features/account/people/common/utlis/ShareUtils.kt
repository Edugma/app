package io.edugma.features.account.people.common.utlis

import io.edugma.domain.account.model.student.Student

fun Collection<Student>.convertAndShare(): String {
    return share { index, student -> "${index + 1}. ${student.getFullName()}" }
}

fun<T> Collection<T>.share(mapper: (Int, T) -> String): String {
    return mapIndexed { index: Int, t: T -> mapper(index, t) }.joinToString("\n")
}
