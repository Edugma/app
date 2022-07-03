package io.edugma.domain.schedule.model.group

import io.edugma.domain.schedule.model.StudentDirection
import io.edugma.domain.schedule.model.StudentFaculty
import kotlinx.serialization.Serializable

@Serializable
data class GroupInfo(
    val id: String,
    val title: String,
    val course: Int? = null,
    val faculty: StudentFaculty? = null,
    val direction: StudentDirection? = null,
) : Comparable<GroupInfo> {
    override fun compareTo(other: GroupInfo): Int {
        return title.compareTo(other.title)
    }
}

val GroupInfo.description
    get() = buildString {
        course?.let { append("$course-й курс") }

        direction?.let {
            if (isNotEmpty()) append(", ")
            append(direction.title)
        }

        faculty?.let {
            if (isNotEmpty()) append(", ")
            if (faculty.titleShort != null) {
                append(faculty.titleShort)
            } else {
                append(faculty.title)
            }
        }
    }