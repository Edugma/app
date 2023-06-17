package io.edugma.navigation.core.screen

data class ScreenBundle(
    val screen: Screen,
    val args: Map<ScreenArg<*>, Any>,
)

fun <T : Screen> T.bundleOf(vararg args: Pair<ScreenArg<*>, Any>): ScreenBundle {
    return ScreenBundle(
        screen = this,
        args = args.toMap(),
    )
}

infix fun <T : Any> ScreenRequiredArg<T>.set(value: T): Pair<ScreenArg<T>, Any> = Pair(this, value)
infix fun <T : Any> ScreenOptionalArg<T>.set(value: T?): Pair<ScreenArg<T>, Any> {
    return Pair(this, value ?: checkNotNull(this.defaultValue))
}
