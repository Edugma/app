package io.edugma.domain.account.model

import io.edugma.domain.account.model.EducationForm
import io.edugma.domain.account.model.EducationType
import io.edugma.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Student(
    val id: String,
    val firstName: String,
    val secondName: String,
    val surname: String? = null,
    val sex: String,
    val avatar: String? = null,
    @Serializable(LocalDateConverter::class)
    val birthday: LocalDate? = null,
    val faculty: String,
    val direction: String,
    val specialization: String? = null,
    val educationType: EducationType? = null,
    val educationForm: EducationForm? = null,
    val payment: Boolean,
    val course: Int? = null,
    val group: String? = null,
    val years: String? = null,
    val dialogId: String? = null,
    val additionalInfo: String? = null
) {
    fun getFullName() = "$secondName $firstName $surname"
}
