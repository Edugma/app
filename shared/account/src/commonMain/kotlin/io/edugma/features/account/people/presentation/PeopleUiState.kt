package io.edugma.features.account.people.presentation

import androidx.compose.runtime.Immutable
import io.edugma.core.arch.pagination.PaginationUiState
import io.edugma.features.account.domain.model.peoples.Person
import io.edugma.features.account.people.PeopleScreenType

@Immutable
data class PeopleUiState(
    val type: PeopleScreenType? = null,
    val name: String = "",
    val bottomType: BottomSheetType = BottomSheetType.Filter,
    val selectedPerson: Person? = null,
    val paginationState: PaginationUiState<Person> = PaginationUiState.empty(),
)
