package com.edugma.features.account.personal

import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.core.utils.lce.launchLce
import com.edugma.features.account.domain.repository.PersonalRepository

class PersonalViewModel(
    private val repository: PersonalRepository,
) : FeatureLogic<PersonalUiState, PersonalAction>() {
    override fun initialState(): PersonalUiState {
        return PersonalUiState()
    }

    override fun onCreate() {
        load(isRefreshing = false)
    }

    private fun load(isRefreshing: Boolean) {
        launchLce(
            lceProvider = {
                repository.getPersonalInfo(forceUpdate = isRefreshing)
            },
            getLceState = { lceState },
            setLceState = { copy(lceState = it) },
            isContentEmpty = { false },
            isRefreshing = isRefreshing,
            onSuccess = {
                newState {
                    toContent(it.valueOrThrow)
                }
            },
        )
    }

    override fun processAction(action: PersonalAction) {
        when (action) {
            PersonalAction.OnRefresh -> load(isRefreshing = true)
        }
    }

    fun exit() {
        accountRouter.back()
    }
}
