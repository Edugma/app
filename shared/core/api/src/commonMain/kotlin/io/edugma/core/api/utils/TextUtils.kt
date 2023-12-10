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

fun getShortName(fullName: String): String {
    val names = getNames(fullName)
    if (names.isEmpty()) {
        return ""
    }

    return if (!canBeShortened(names)) {
        names.joinToString("\u00A0")
    } else {
        val shortName = StringBuilder(names.first())
        for (i in 1 until names.size) {
            shortName.append("\u00A0")
                .append(names[i].first())
                .append('.')
        }
        shortName.toString()
    }
}

private fun canBeShortened(names: List<String>): Boolean {
    return (names.first().length > 1) &&
        (names.first().let { it[0].isLowerCase() == it[1].isLowerCase() })
}

private fun getNames(name: String): List<String> {
    return name.split(' ', '.')
        .filter { it.isNotEmpty() || it.isNotBlank() }
}
