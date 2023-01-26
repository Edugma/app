package io.edugma.data.base.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata
import kotlin.time.Duration

@Serializable
data class DataVersion(
    override val id: String,
    val dateTime: Instant,
) : Metadata

fun DataVersion.isExpired(duration: Duration): Boolean {
    return dateTime + duration <= Clock.System.now()
}
