package com.edugma.navigation.core.destination

public class NavArgs<T : Destination>(
    public val destination: T,
    @PublishedApi
    internal val argumentsStore: ArgumentsStore,
) {
    public inline operator fun <Y> invoke(action: NavArgs<T>.() -> Y): Y {
        return action()
    }

    /**
     * Возвращает значение аргумента.
     */
    public inline fun <reified T : Any> NavArgument.Required<T>.get(): T {
        val value = argumentsStore.get<T>(this.name, T::class)
        checkNotNull(value) { "Обязательный аргумент должен быть установлен до момента получения" }
        return value
    }

    /**
     * Возвращает значение аргумента.
     */
    public inline fun <reified T : Any> NavArgument.Optional<T>.get(): T {
        val value = argumentsStore.get<T>(this.name, T::class)
        checkNotNull(value) { "У опционального аргумента должно быть значение по умолчанию" }
        return value
    }

    /**
     * Возвращает значение аргумента.
     */
    public inline fun <reified T : Any> NavArgument.NullableOptional<T>.get(): T? {
        return argumentsStore.get<T>(name, T::class)
    }
}
