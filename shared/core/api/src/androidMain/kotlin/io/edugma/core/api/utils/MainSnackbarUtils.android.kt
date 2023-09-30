package io.edugma.core.api.utils

import java.nio.channels.UnresolvedAddressException

internal actual fun getKnownException(e: Throwable): KnownException {
    return when (e) {
        is UnresolvedAddressException -> KnownException.NetworkException(
            message = e.message.orEmpty(),
            isDeviceProblem = true,
        )

        else -> getCommonKnownException(e)
    }
}
