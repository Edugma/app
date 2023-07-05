package io.edugma.navigation.core.screen

class ScreenOptionalArg<T : Any>(
    name: String,
    type: ArgumentType,
    defaultValue: T?,
) : ScreenArg<T>(
    name = name,
    type = type,
    defaultValue = defaultValue,
)

class ScreenRequiredArg<T : Any>(
    name: String,
    type: ArgumentType,
) : ScreenArg<T>(
    name = name,
    type = type,
    defaultValue = null,
)

abstract class ScreenArg<T : Any>(
    val name: String,
    val type: ArgumentType,
    val defaultValue: T?,
)

enum class ArgumentType {
    STRING,
    INT,
    BOOLEAN,
    FLOAT,
    SERIALIZABLE,
}
