package io.edugma.features.account.domain.usecase

import io.edugma.features.account.domain.model.Contracts
import io.edugma.features.account.domain.model.Performance
import io.edugma.features.account.domain.model.Personal
import kotlin.math.roundToInt

class MenuDataConverterUseCase {

    fun convert(personal: Personal): PersonalData {
        return personal.toPersonalData()
    }

    fun convert(contracts: Contracts): CurrentPayments? {
        return contracts.getCurrent()
    }

    fun convert(performance: List<Performance>): CurrentPerformance? {
        return performance.getCurrent()
    }

    private fun Personal.toPersonalData(): PersonalData {
        return PersonalData(
            label = "$degreeLevel $course курса группы $group",
            specialization = specialty,
            avatar = avatar,
            fullName = getFullName(),
        )
    }

    private fun Contracts.getCurrent(): CurrentPayments? {
        return contracts.entries.firstOrNull()?.let {
            val sum = it.value.sum.toIntOrNull() ?: return null
            val current = sum - (it.value.balance.toIntOrNull() ?: return null)
            val debt = (it.value.balanceCurrent.toIntOrNull() ?: return null) > 0
            CurrentPayments(
                type = it.key,
                sum = sum,
                current = current,
                debt = debt,
            )
        }
    }

    private fun List<Performance>.getCurrent(): CurrentPerformance? {
        fun getSortedMarks(marks: List<Performance>): Map<String, Int> {
            val marksList = marks.map { it.grade }.filterNot { it == "Зачтено" || it == "Не зачтено" }.let { list ->
                mutableMapOf<String, Int>().apply {
                    list.toSet().forEach { put(it, list.count { mark -> mark == it }) }
                    keys.forEach { this[it] = ((this[it] ?: 0).toDouble() / list.size * 100).roundToInt() }
                }
                    .filterNot { it.value == 0 }
            }
            val resultMap = mutableMapOf<String, Int>()
            marksList.values.sorted().reversed().take(3).forEach {
                marksList.forEach { (mark, percent) -> if (it == percent) resultMap[mark] = percent }
            }
            while (resultMap.keys.size > 3) {
                resultMap.remove(resultMap.keys.last())
            }
            return resultMap
        }
        if (isEmpty()) return null
        return CurrentPerformance(
            first().semester,
            getSortedMarks(filter { it.semester == first().semester }),
            getSortedMarks(this),
        )
    }
}

data class PersonalData(
    val label: String,
    val specialization: String?,
    val avatar: String?,
    val fullName: String,
)

data class CurrentPerformance(
    val lastSemesterNumber: Int,
    val lastSemester: Map<String, Int>,
    val allSemesters: Map<String, Int>,
)

data class CurrentPayments(
    val type: io.edugma.features.account.domain.model.PaymentType,
    val sum: Int,
    val current: Int,
    val debt: Boolean,
)
