package io.edugma.core.designSystem.molecules.avatar

fun String.toAvatarInitials(maxLength: Int = 2): String {
    return split("""[\s-]""".toRegex())
        .take(maxLength)
        .joinToString(separator = "") { it[0].toString() }
        .uppercase()
}
