package com.edugma.features.account.domain.usecase

import com.edugma.features.account.domain.model.Personal
import com.edugma.features.account.domain.model.accounts.AccountModel
import com.edugma.features.account.domain.model.payments.Contract
import com.edugma.features.account.domain.model.performance.GradePosition
import kotlin.math.roundToInt

class MenuDataConverterUseCase {

    fun convert(personal: Personal): SelectedAccountUiModel {
        return personal.toPersonalData()
    }

    fun convert(account: AccountModel): SelectedAccountUiModel {
        return SelectedAccountUiModel(
            description = account.description,
            avatar = account.avatar,
            name = account.name,
        )
    }

    fun convert(contracts: List<Contract>): CurrentPayments? {
        return contracts.getCurrent()
    }

    fun convert(performance: List<GradePosition>): CurrentPerformance? {
        return performance.getCurrent()
    }

    private fun Personal.toPersonalData(): SelectedAccountUiModel {
        return SelectedAccountUiModel(
            description = description,
            avatar = avatar,
            name = name,
        )
    }

    private fun List<Contract>.getCurrent(): CurrentPayments? {
        return firstOrNull()?.let {
            val sum = it.balance.toIntOrNull() ?: return null
            val current = sum - (it.balance.toIntOrNull() ?: return null)
            val debt = (it.balance.toIntOrNull() ?: return null) > 0
            CurrentPayments(
                type = it.title,
                sum = sum,
                current = current,
                debt = debt,
            )
        }
    }

    private fun List<GradePosition>.getCurrent(): CurrentPerformance? {
        fun getSortedMarks(marks: List<GradePosition>): Map<String, Int> {
            val marksList = marks.map { it.grade?.value?.toString().orEmpty() }.let { list ->
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
            0,
            emptyMap(),
            getSortedMarks(this),
        )
    }
}

data class SelectedAccountUiModel(
    val description: String,
    val avatar: String?,
    val name: String,
)

data class CurrentPerformance(
    val lastSemesterNumber: Int,
    val lastSemester: Map<String, Int>,
    val allSemesters: Map<String, Int>,
)

data class CurrentPayments(
    val type: String,
    val sum: Int,
    val current: Int,
    val debt: Boolean,
)
