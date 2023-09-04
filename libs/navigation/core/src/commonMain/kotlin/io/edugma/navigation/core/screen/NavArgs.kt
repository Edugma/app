package io.edugma.navigation.core.screen

class NavArgs<T : Screen>(
    @PublishedApi
    internal val screenBundle: ScreenBundle,
) {
    @Suppress("UNCHECKED_CAST")
    val screen
        get() = screenBundle.screen as T

    @Suppress("UNCHECKED_CAST")
    fun <TArg : Any> ScreenArg<TArg>.get(): TArg {
        return screenBundle.args[this] as TArg
    }

    inline operator fun <TArg : Any> invoke(action: NavArgs<T>.() -> TArg): TArg {
        return action(this)
    }
}
