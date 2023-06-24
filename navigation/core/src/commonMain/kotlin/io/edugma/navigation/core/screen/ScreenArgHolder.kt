package io.edugma.navigation.core.screen

class ScreenArgHolder<T : Screen>(
    @PublishedApi
    internal val screenBundle: ScreenBundle,
) {
    val screen
        get() = screenBundle.screen as T

    fun <TArg : Any> ScreenArg<TArg>.get(): TArg {
        return screenBundle.args[this] as TArg
    }
}
