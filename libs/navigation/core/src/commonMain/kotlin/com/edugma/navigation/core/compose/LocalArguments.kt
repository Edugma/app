package com.edugma.navigation.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.edugma.navigation.core.destination.ArgumentsStore
import com.edugma.navigation.core.destination.Destination
import com.edugma.navigation.core.destination.NavArgs

val LocalArguments: ProvidableCompositionLocal<ArgumentsStore?> = staticCompositionLocalOf { null }

@Composable
fun <T : Destination> rememberNavArgs(destination: T): NavArgs<T> {
    val arguments = checkNotNull(LocalArguments.current) { "Не найдены аргументы для $destination" }

    return remember(destination, arguments) {
        NavArgs(destination, arguments)
    }
}
