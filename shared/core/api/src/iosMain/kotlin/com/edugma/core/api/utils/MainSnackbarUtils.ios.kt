package com.edugma.core.api.utils

import io.ktor.util.network.UnresolvedAddressException

internal actual fun getKnownException(e: Throwable): KnownException {
    return when (e) {
        is UnresolvedAddressException -> KnownException.NetworkException(
            message = e.message.orEmpty(),
            isDeviceProblem = true,
        )

        else -> getCommonKnownException(e)
    }
}
