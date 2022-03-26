package io.edugma.data.base.model

import io.edugma.domain.base.utils.converters.ZonedDateTimeConverter
import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@Serializable
data class DataVersion(
    override val id: String,
    @Serializable(with = ZonedDateTimeConverter::class)
    val dateTime: ZonedDateTime
) : Metadata

fun DataVersion.isExpired(duration: Duration): Boolean {
    return dateTime.plus(duration.toJavaDuration()) <= ZonedDateTime.now()
}