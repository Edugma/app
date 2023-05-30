package io.edugma.features.base.core.navigation.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import co.touchlab.kermit.Logger
import io.edugma.features.base.core.navigation.core.Screen
import io.edugma.features.base.core.navigation.core.ScreenInfo
import io.edugma.features.base.core.navigation.core.ScreenInfoSerializer
import io.ktor.util.*
import kotlin.reflect.KClass

inline fun <reified T : Screen> NavGraphBuilder.addScreen(
    deepLinks: List<NavDeepLink> = emptyList(),
    crossinline content: @Composable Screen.() -> Unit,
) {
    val route = getRoute<T>()
    val fullRoute = "$route?screen={screen}"
    val navArgument = navArgument("screen") { defaultValue = "" }

    composable(fullRoute, listOf(navArgument), deepLinks) {
        val args = it.arguments?.getString("screen")
            ?.let { toScreenInfo(it) }
            ?: ScreenInfo("", emptyMap())

        content(args)
    }
}

inline fun <reified TRoute : Screen, reified TStart : Screen> NavGraphBuilder.groupScreen(
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline builder: NavGraphBuilder.() -> Unit,
) {
    val startDestination = "${getRoute<TStart>()}?screen={screen}"

    navigation(startDestination, getRoute<TRoute>(), arguments, deepLinks, builder)
}

fun screens(
    builder: NavGraphBuilder.() -> Unit,
): NavGraphBuilder.() -> Unit {
    return builder
}

private fun String.encodeUrl(): String {
    return this.replace("+", "%2B")
        .replace("/", "%2F")
        .replace("=", "%3D")
}

fun Screen.getRoute() =
    this.key.replace(".", "-")

fun Screen.getUrlArgs(): String {
    val serialized = ScreenInfoSerializer.serialize(this)
    Logger.d("serialized: $serialized", tag = "Navigation")
    return serialized
        .encodeBase64()
        .encodeUrl()
}

fun Screen.getFullRoute(): String {
    val args = getUrlArgs()
    return "${getRoute()}?screen=$args"
}

fun Screen.getFullRawRoute(): String {
    return "${getRoute()}?screen={screen}"
}

fun toScreenInfo(text: String): ScreenInfo? {
    val decoded = text.decodeBase64String()
    Logger.d("decoded: $decoded", tag = "Navigation")
    return ScreenInfoSerializer.deserialize(decoded)
}

inline fun <reified T : Screen> getRoute() =
    T::class.qualifiedName?.replace(".", "-") ?: ""

fun <T : Screen> KClass<T>.getRoute() =
    qualifiedName?.replace(".", "-") ?: ""
