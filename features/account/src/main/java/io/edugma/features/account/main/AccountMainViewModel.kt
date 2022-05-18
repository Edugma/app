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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AccountMainViewModel(
    private val personalRepository: PersonalRepository,
    private val performanceRepository: PerformanceRepository
) : BaseViewModel<AccountMenuState>(AccountMenuState()) {

    init {
        load()
        viewModelScope.launch {
            screenResultProvider.getEvents().collectLatest {
                when (it) {
                    UpdateMenu -> load()
                }
            }
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

    private fun List<Performance>.getCurrent(): CurrentPerformance {
        val lasts = filter { it.semester == first().semester }.map { it.grade }.let { list ->
            mutableMapOf<String, Int>().apply {
                list.forEach { put(it, (this[it] ?: 0) + 1) }
                keys.forEach { this[it] = (this[it] ?: 0) / list.size * 100 }
                filterNot { it.value == 0 }
            }
        }
        val all = map { it.grade }.let { list ->
            mutableMapOf<String, Int>().apply {
                list.forEach { put(it, (this[it] ?: 0) + 1) }
                keys.forEach { this[it] = (this[it] ?: 0) / list.size * 100 }
                filterNot { it.value == 0 }
            }
        }
        return CurrentPerformance(lasts, all)
    }
}

data class AccountMenuState(
    val menu: List<MenuUi> = listOf(Auth,
        Personal, Students, Teachers, Classmates, Payments, Applications, Marks),
    val personal: io.edugma.domain.account.model.Personal? = null,
    val performance: CurrentPerformance? = null
)

data class CurrentPerformance(
    val lastSemester: Map<String, Int>,
    val allSemesters: Map<String, Int>
)

object UpdateMenu: ScreenResult