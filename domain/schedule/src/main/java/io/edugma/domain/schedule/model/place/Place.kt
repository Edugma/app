package io.edugma.domain.schedule.model.place

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val id: String,
    val title: String,
    val type: PlaceType
) : Comparable<Place> {
    override fun compareTo(other: Place): Int {
        return title.compareTo(other.title)
    }
}