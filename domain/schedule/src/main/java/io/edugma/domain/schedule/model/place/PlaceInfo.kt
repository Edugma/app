package io.edugma.domain.schedule.model.place

import io.edugma.domain.base.model.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PlaceInfo : Comparable<PlaceInfo> {
    abstract val id: String
    abstract val title: String

    private fun getTypeNumber(): Int {
        return when (this) {
            is Building -> 0
            is Online -> 1
            is Other -> 2
            is Unclassified -> 3
        }
    }

    override fun compareTo(other: PlaceInfo): Int {
        val compareTypes = getTypeNumber().compareTo(other.getTypeNumber())
        return if (compareTypes != 0) {
            compareTypes
        } else {
            title.compareTo(other.title)
        }
    }

    @Serializable
    @SerialName("building")
    data class Building(
        override val id: String,
        override val title: String,
        val areaAlias: String? = null,
        val street: String? = null,
        val building: String? = null,
        val floor: String? = null,
        val auditorium: String? = null,
        val location: Location? = null,
        val description: Map<String, String>? = null
    ) : PlaceInfo()

    @Serializable
    @SerialName("online")
    data class Online(
        override val id: String,
        override val title: String,
        val url: String? = null,
        val description: Map<String, String>? = null
    ) : PlaceInfo()

    @Serializable
    @SerialName("other")
    data class Other(
        override val id: String,
        override val title: String,
        val description: Map<String, String>? = null
    ) : PlaceInfo()

    @Serializable
    @SerialName("unclassified")
    data class Unclassified(
        override val id: String,
        override val title: String,
        val description: Map<String, String>? = null
    ) : PlaceInfo()
}