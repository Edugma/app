package io.edugma.features.account.people.presentation

sealed interface PeopleAction {
    data class OnQuery(val query: String) : PeopleAction
    data object OnRefresh : PeopleAction
    data object OnLoadNextPage : PeopleAction
}
