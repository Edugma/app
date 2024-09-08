package com.edugma.navigation.core.utils

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDestination
import androidx.navigation.NavDestinationBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.edugma.navigation.core.destination.ArgumentType
import com.edugma.navigation.core.destination.Destination
import com.edugma.navigation.core.destination.DestinationBundle
import com.edugma.navigation.core.destination.NavArgument
import com.edugma.navigation.core.graph.NavGraph

fun <D : NavDestination> NavDestinationBuilder<D>.argument(argument: NavArgument<*>) {
    argument(argument.name) {
        type = getType(argument.type)
        nullable = argument is NavArgument.NullableOptional

        when (argument) {
            is NavArgument.Optional -> defaultValue = argument.defaultValue
            is NavArgument.NullableOptional -> defaultValue = argument.defaultValue
            else -> { }
        }
    }
}

fun getNamedNavArgument(argument: NavArgument<*>): NamedNavArgument {
    return navArgument(argument.name) {
        type = getType(argument.type)
        nullable = argument is NavArgument.NullableOptional

        when (argument) {
            is NavArgument.Optional -> defaultValue = argument.defaultValue
            is NavArgument.NullableOptional -> defaultValue = argument.defaultValue
            else -> {}
        }
    }
}

// fun getDeeplink(deeplink: NavDeepLink): JetpackNavDeepLink =
//     androidx.navigation.NavDeepLink(
//         uriPattern = deeplink.uriPattern,
//             action = deeplink.action,
//             mimeType = deeplink.mimeType,
//     )

private fun getType(argumentType: ArgumentType): NavType<*> {
    return when (argumentType) {
        ArgumentType.BOOLEAN -> NavType.BoolType
        ArgumentType.INT -> NavType.IntType
        ArgumentType.LONG -> NavType.LongType
        ArgumentType.FLOAT -> NavType.FloatType
        ArgumentType.STRING -> NavType.StringType
    }
}

/**
 * Возвращает полный путь с аргументами.
 */
public fun Destination.getRoute(): String {
    val args = getArgs().sortedBy { it.name }

    val name = destinationName

    val argsString = if (args.isEmpty()) {
        ""
    } else {
        args.joinToString(separator = "&", prefix = "?") { "${it.name}={${it.name}}" }
    }

    return name + argsString
}

/**
 * Возвращает полный путь.
 */
public fun NavGraph.getRoute(): String {
    return destinationName
}

/**
 * Возвращает полный путь с заполненными аргументами.
 */
public fun <T : Destination> DestinationBundle<T>.getRoute(): String {
    val name = destination.destinationName

    val args = arguments.filter { it.value != null }.toList().sortedBy { it.first.name }
    val argsString = if (args.isEmpty()) {
        ""
    } else {
        args.joinToString(separator = "&", prefix = "?") {
            "${it.first.name}=${it.second.toUrlEncodedString()}"
        }
    }

    return name + argsString
}

private fun <T> T.toUrlEncodedString(): String {
    return if (this is String) this.toUrlEncoded() else this.toString()
}

internal fun String.toUrlEncoded(): String {
    val result = StringBuilder()
    for (char in this) {
        when {
            char in 'a'..'z' || char in 'A'..'Z' || char in '0'..'9' -> result.append(char)
            char == ' ' -> result.append("%20")
            else -> result.append("%${char.code.toByte().toString(16).uppercase()}")
        }
    }
    return result.toString()
}

internal fun String.toUrlDecoded(): String {
    val result = StringBuilder()
    var index = 0
    while (index < this.length) {
        when {
            this[index] == '%' -> {
                val hex = this.substring(index + 1, index + 3)
                val charCode = hex.toInt(16).toChar()
                result.append(charCode)
                index += 3 // Перемещаемся за обработанный символ
            }
            else -> {
                result.append(this[index])
                index++
            }
        }
    }
    return result.toString()
}
