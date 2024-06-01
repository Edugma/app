package com.edugma.features.account.people.presentation

import com.edugma.features.account.domain.model.peoples.Person
import com.edugma.features.account.people.PeopleScreenType

sealed interface PeopleAction {
    data class OnQuery(val query: String) : PeopleAction
    data object OnRefresh : PeopleAction
    data object OnLoadNextPage : PeopleAction
    data class OnArgs(val type: PeopleScreenType) : PeopleAction
    data object OnSearchClick : PeopleAction
    data object Back : PeopleAction
    data object OnSelectFilter : PeopleAction
    data class OnSelectPerson(val person: Person) : PeopleAction
    data object OnShareClick : PeopleAction
    data object OnOpenSchedule : PeopleAction
    data object OnBottomSheetClosed : PeopleAction
}
