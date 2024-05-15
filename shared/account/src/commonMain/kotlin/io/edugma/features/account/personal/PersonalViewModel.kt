package io.edugma.features.account.personal

import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.utils.lce.launchLce
import io.edugma.features.account.domain.repository.PersonalRepository

class PersonalViewModel(
    private val repository: PersonalRepository,
) : BaseActionViewModel<PersonalUiState, PersonalAction>(PersonalUiState()) {

    init {
        load(isRefreshing = false)
    }

    private fun load(isRefreshing: Boolean) {
        launchLce(
            lceProvider = {
                repository.getPersonalInfo(forceUpdate = isRefreshing)
            },
            getLceState = state::lceState,
            setLceState = { newState { copy(lceState = it) } },
            isContentEmpty = { false },
            isRefreshing = isRefreshing,
            onSuccess = {
                newState {
                    toContent(it.valueOrThrow)
                }
            },
        )
    }

    override fun onAction(action: PersonalAction) {
        when (action) {
            PersonalAction.OnRefresh -> load(isRefreshing = true)
        }
    }

    fun exit() {
        accountRouter.back()
    }
}
