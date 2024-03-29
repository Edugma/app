package io.edugma.core.api.model

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

        return timestamp.plus(expiresIn) > now
    }
}
