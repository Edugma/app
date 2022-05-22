package io.edugma.data.base.model

import io.edugma.domain.base.utils.converters.ZonedDateTimeConverter
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@Serializable
data class DataVersion(
    override val id: String,
    val dateTime: Instant
) : Metadata

fun DataVersion.isExpired(duration: Duration): Boolean {
    return dateTime + duration <= Clock.System.now()
}