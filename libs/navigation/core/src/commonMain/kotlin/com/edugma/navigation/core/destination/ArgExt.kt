package com.edugma.navigation.core.destination

inline fun <reified T : Any> Destination.reqArg(
    name: String,
): NavArgument.Required<T> = createRequiredArgument<T>(
    name = name,
    type = inferArgumentType<T>(),
)

inline fun <reified T : Any> Destination.optArg(
    name: String,
    defaultValue: T,
): NavArgument.Optional<T> = createOptionalArgument(
    name = name,
    defaultValue = defaultValue,
    type = inferArgumentType<T>(),
)

fun Destination.optNullableArg(
    name: String,
    defaultValue: String? = null,
): NavArgument.NullableOptional<String> = createNullableOptionalArgument(
    name = name,
    defaultValue = defaultValue,
    type = ArgumentType.STRING,
)

fun <T : Any> Destination.createRequiredArgument(
    name: String,
    type: ArgumentType,
): NavArgument.Required<T> = NavArgument.Required<T>(
    name = name,
    type = type,
).also {
    arguments += it
}

fun <T : Any> Destination.createOptionalArgument(
    name: String,
    defaultValue: T,
    type: ArgumentType,
): NavArgument.Optional<T> = NavArgument.Optional(
    name = name,
    defaultValue = defaultValue,
    type = type,
).also {
    arguments += it
}

fun Destination.createNullableOptionalArgument(
    name: String,
    defaultValue: String?,
    type: ArgumentType,
): NavArgument.NullableOptional<String> = NavArgument.NullableOptional(
    name = name,
    defaultValue = defaultValue,
    type = type,
).also {
    arguments += it
}

inline fun <reified T> inferArgumentType(): ArgumentType {
    return when (T::class) {
        Int::class -> ArgumentType.INT
        Long::class -> ArgumentType.LONG
        Float::class -> ArgumentType.FLOAT
        Boolean::class -> ArgumentType.BOOLEAN
        String::class -> ArgumentType.STRING
        else -> {
            throw IllegalArgumentException(
                "Объект типа ${T::class.qualifiedName} не поддерживается в качестве аргумента.",
            )
        }
    }
}

