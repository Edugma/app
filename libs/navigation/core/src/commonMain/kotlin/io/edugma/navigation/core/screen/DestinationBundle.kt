package io.edugma.navigation.core.screen

inline fun <T : Destination> T.toBundle(action: DestinationBundle<T>.() -> Unit = {}): DestinationBundle<T> {
    return DestinationBundle(destination = this).apply {
        action()
        checkRequiredArgs()
    }
}

class DestinationBundle<T : Destination>(
    public val destination: T,
) {
    private val _arguments: MutableMap<NavArgument<*>, Any?> = hashMapOf()

    public val arguments: Map<NavArgument<*>, Any?> = _arguments

    init {
        destination.arguments.forEach {
            when (it) {
                is NavArgument.Optional -> {
                    _arguments[it] = it.defaultValue
                }
                is NavArgument.NullableOptional -> {
                    _arguments[it] = it.defaultValue
                }
                is NavArgument.Required -> {
                    _arguments[it] = null
                }
            }
        }
    }

    public infix fun <T : Any> NavArgument.Required<T>.set(value: T) {
        _arguments[this] = value
    }

    public infix fun <T : Any> NavArgument.Optional<T>.set(value: T?) {
        value?.let { _arguments[this] = value }
    }

    public infix fun <T : Any> NavArgument.NullableOptional<T>.set(value: T?) {
        value?.let { _arguments[this] = value }
    }

    @Suppress("UNCHECKED_CAST")
    public fun <T : Any> NavArgument.Required<T>.get(): T {
        val value = _arguments[this]
        checkNotNull(value) { "Обязательный аргумент должен быть установлен до момента получения" }
        return value as T
    }

    @Suppress("UNCHECKED_CAST")
    public fun <T> NavArgument.Optional<T>.get(): T {
        return _arguments[this] as T
    }

    @Suppress("UNCHECKED_CAST")
    public fun <T> NavArgument.NullableOptional<T>.get(): T? {
        return _arguments[this] as T?
    }

    fun checkRequiredArgs() {
        val allRequiredArgumentsAreSet = _arguments.filterKeys(NavArgument<*>::isRequired).all { it.value != null }
        check(allRequiredArgumentsAreSet) {
            val notSetRequiredArguments = _arguments
                .filter { it.key.isRequired && it.value == null }
                .map { it.key.name }
            "Не переданы все обязательные аргументы. " +
                "Экран: ${destination.destinationName}. " +
                "Ненайденные обязательные аргументы: $notSetRequiredArguments"
        }
    }
}

