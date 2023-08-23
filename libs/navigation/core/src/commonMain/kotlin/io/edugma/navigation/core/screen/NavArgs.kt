package io.edugma.navigation.core.screen

class NavArgs<T : Screen>(
    @PublishedApi
    internal val screenBundle: ScreenBundle,
) {
    val screen
        get() = screenBundle.screen as T

    fun <TArg : Any> ScreenArg<TArg>.get(): TArg {
        return screenBundle.args[this] as TArg
    }

    inline operator fun <TArg : Any> invoke(action: NavArgs<T>.() -> TArg): TArg {
        return action(this)
    }
}
