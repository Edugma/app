package io.edugma.features.account.main

import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Performance
import io.edugma.domain.account.repository.PerformanceRepository
import io.edugma.domain.account.repository.PersonalRepository
import io.edugma.features.account.main.model.MenuUi
import io.edugma.features.account.main.model.MenuUi.*
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.mvi.BaseViewModelFull
import io.edugma.features.base.core.utils.ScreenResult
import io.edugma.features.base.navigation.AccountScreens
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.isActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

class AccountMainViewModel(
    private val personalRepository: PersonalRepository,
    private val performanceRepository: PerformanceRepository
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
            val info = personalRepository.getLocalPersonalInfo()
            mutateState {
                state = state.copy(personal = info)
            }
        }
        viewModelScope.launch {
            val performance = performanceRepository.getLocalMarks()
            mutateState {
                state = state.copy(performance = performance?.getCurrent())
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
    val bottomMenu: List<MenuUi> = listOf(Students, Teachers, Classmates, Applications),
    val personal: io.edugma.domain.account.model.Personal? = null,
    val performance: CurrentPerformance? = null,
    val showCurrentPerformance: Boolean = true
)

data class CurrentPerformance(
    val lastSemesterNumber: Int,
    val lastSemester: Map<String, Int>,
    val allSemesters: Map<String, Int>
)

object UpdateMenu: ScreenResult