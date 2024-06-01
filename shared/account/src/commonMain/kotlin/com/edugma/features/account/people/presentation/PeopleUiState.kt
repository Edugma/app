package com.edugma.features.account.people.presentation

import androidx.compose.runtime.Immutable
import com.edugma.core.arch.pagination.PaginationUiState
import com.edugma.features.account.domain.model.peoples.Person
import com.edugma.features.account.people.PeopleScreenType

@Immutable
data class PeopleUiState(
    val type: PeopleScreenType? = null,
    val name: String = "",
    val showBottomSheet: Boolean = false,
    val bottomType: BottomSheetType = BottomSheetType.Filter,
    val selectedPerson: Person? = null,
    val paginationState: PaginationUiState<Person> = PaginationUiState.empty(),
)
