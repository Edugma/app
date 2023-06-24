package io.edugma.navigation.core.screen

public open abstract class Screen(val name: String) {
    private val arguments = mutableListOf<ScreenArg<*>>()

    protected inline fun <reified T : Any> reqArg(name: String): ScreenRequiredArg<T> {
        val argType = getArgType<T>()
        return reqArg(name, argType)
    }

    @PublishedApi
    internal fun <T : Any> reqArg(name: String, type: ArgumentType): ScreenRequiredArg<T> {
        val arg = ScreenRequiredArg<T>(
            name = name,
            type = type,
        )
        arguments += arg
        return arg
    }

    protected fun <T : Any> optArg(name: String, defaultValue: T): ScreenOptionalArg<T> {
        val argType = getArgType(defaultValue)
        val arg = ScreenOptionalArg(
            name = name,
            type = argType,
            defaultValue = defaultValue,
        )
        arguments += arg
        return arg
    }
}

public open abstract class NoArgScreen(name: String) : Screen(name) {
    public operator fun invoke(): ScreenBundle = bundleOf()
}

@PublishedApi
internal inline fun <reified T> getArgType(): ArgumentType {
    return when (T::class) {
        String::class -> ArgumentType.STRING
        Int::class -> ArgumentType.INT
        Boolean::class -> ArgumentType.BOOLEAN
        Float::class -> ArgumentType.FLOAT
        else -> ArgumentType.SERIALIZABLE
    }
}

private fun <T> getArgType(value: T): ArgumentType {
    return when (value) {
        is String -> ArgumentType.STRING
        is Int -> ArgumentType.INT
        is Boolean -> ArgumentType.BOOLEAN
        is Float -> ArgumentType.FLOAT
        else -> ArgumentType.SERIALIZABLE
    }
}
