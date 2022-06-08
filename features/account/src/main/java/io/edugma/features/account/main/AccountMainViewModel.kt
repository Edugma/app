package io.edugma.features.account.main

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Contracts
import io.edugma.domain.account.model.PaymentType
import io.edugma.domain.account.model.Performance
import io.edugma.domain.account.repository.AuthorizationRepository
import io.edugma.domain.account.repository.PaymentsRepository
import io.edugma.domain.account.repository.PerformanceRepository
import io.edugma.domain.account.repository.PersonalRepository
import io.edugma.features.account.main.model.MenuUi
import io.edugma.features.account.main.model.MenuUi.*
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.utils.ScreenResult
import io.edugma.features.base.core.utils.isNotNull
import io.edugma.features.base.navigation.AccountScreens
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

class AccountMainViewModel(
    private val personalRepository: PersonalRepository,
    private val performanceRepository: PerformanceRepository,
    private val paymentsRepository: PaymentsRepository,
    private val authorizationRepository: AuthorizationRepository,
) : BaseViewModel<AccountMenuState>(AccountMenuState()) {

    private val performanceTimer = flow {
        var bool = Random.nextBoolean()
        emit(bool)
        while (true) {
            if (!currentCoroutineContext().isActive)
                return@flow
            else {
                delay(10000)
                bool = !bool
                emit(bool)
            }
        }
    }

    init {
        load()
        viewModelScope.launch {
            screenResultProvider.getEvents().collectLatest {
                when (it) {
                    UpdateMenu -> load()
                }
            }
        }
        viewModelScope.launch {
            performanceTimer.collectLatest(::changeCurrentPerformance)
        }
    }

    private fun load() {
        viewModelScope.launch {
            val isAuthorized = authorizationRepository.getSavedToken().isNotNull()
            mutateState {
                state = state.copy(isAuthorized = isAuthorized)
            }

        }
        viewModelScope.launch {
            val info = personalRepository.getLocalPersonalInfo()
            mutateState {
                state = state.copy(personal = info?.toPersonalData())
            }
        }
        viewModelScope.launch {
            val performance = performanceRepository.getLocalMarks()
            mutateState {
                state = state.copy(performance = performance?.getCurrent())
            }
        }
        viewModelScope.launch {
            val payments = paymentsRepository.getPaymentsLocal()
            mutateState {
                state = state.copy(currentPayments = payments?.getCurrent())
            }
        }
    }

    fun navigateToAuth() {
        router.navigateTo(AccountScreens.Authorization)
    }

    fun navigateToApplications() {
        router.navigateTo(AccountScreens.Applications)
    }

    fun navigateToPayments() {
        router.navigateTo(AccountScreens.Payments)
    }

    fun navigateToStudents() {
        router.navigateTo(AccountScreens.Students)
    }

    fun navigateToTeachers() {
        router.navigateTo(AccountScreens.Teachers)
    }

    fun navigateToClassmates() {
        router.navigateTo(AccountScreens.Classmates)
    }

    fun navigateToMarks() {
        router.navigateTo(AccountScreens.Marks)
    }

    fun navigateToPersonal() {
        router.navigateTo(AccountScreens.Personal)
    }

    private fun changeCurrentPerformance(isCurrent: Boolean) {
        mutateState {
            state = state.copy(showCurrentPerformance = isCurrent)
        }
    }

    private fun io.edugma.domain.account.model.Personal.toPersonalData(): PersonalData {
        return PersonalData(
            label = "$degreeLevel $course курса группы $group",
            specialization = specialty,
            avatar = avatar,
            fullName = getFullName()
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
                debt = debt
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
            while(resultMap.keys.size > 3) {
                resultMap.remove(resultMap.keys.last())
            }
            return resultMap
        }
        if (isEmpty()) return null
        return CurrentPerformance(
            first().semester,
            getSortedMarks(filter { it.semester == first().semester }),
            getSortedMarks(this)
        )
    }
}

data class AccountMenuState(
    val topMenu: List<MenuUi> = listOf(Auth, Personal, Payments, Marks),
    val bottomMenu: List<MenuUi> = listOf(Students, Teachers, Classmates),
    val personal: PersonalData? = null,
    val performance: CurrentPerformance? = null,
    val currentPayments: CurrentPayments? = null,
    val showCurrentPerformance: Boolean = true,
    val isAuthorized: Boolean = true
)

data class PersonalData(
    val label: String,
    val specialization: String?,
    val avatar: String?,
    val fullName: String
)

data class CurrentPerformance(
    val lastSemesterNumber: Int,
    val lastSemester: Map<String, Int>,
    val allSemesters: Map<String, Int>
)

data class CurrentPayments(
    val type: PaymentType,
    val sum: Int,
    val current: Int,
    val debt: Boolean
)

object UpdateMenu: ScreenResult