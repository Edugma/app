package io.edugma.core.api.utils

/**
 * Replacement for Kotlin's deprecated `capitalize()` function.
 */
fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase()
        } else {
            it.toString()
        }
    }
}

fun getInitials(fullName: String): String {
    val names = fullName.split(" ").filter { it.isNotEmpty() }

    return buildString {
        for (i in 0..names.lastIndex.coerceAtMost(1)) {
            val name = names[i]
            append((name.first()))
        }
    }
}
