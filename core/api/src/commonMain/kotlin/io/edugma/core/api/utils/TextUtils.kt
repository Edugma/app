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
