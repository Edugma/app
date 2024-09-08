package com.edugma.features.account.people.common.utlis

import com.edugma.features.account.domain.model.peoples.Person

fun Collection<Person>.convertAndShare(): String {
    return share { index, student -> "${index + 1}. ${student.name}" }
}

fun <T> Collection<T>.share(mapper: (Int, T) -> String): String {
    return mapIndexed { index: Int, t: T -> mapper(index, t) }.joinToString("\n")
}
