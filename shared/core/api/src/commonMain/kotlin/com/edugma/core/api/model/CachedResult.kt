package com.edugma.core.api.model

import co.touchlab.kermit.Logger
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class CachedResult<T>(
    val data: T,
    val timestamp: Instant,
) {
    fun isExpired(expiresIn: Duration): Boolean {
        val now = Clock.System.now()
        val expiredTime = timestamp.plus(expiresIn)
        // TODO TEST123
        Logger.d("IsExpired: now=$now, expiredTime=$expiredTime", tag = "Store")

        return expiredTime <= now || true
    }
}
